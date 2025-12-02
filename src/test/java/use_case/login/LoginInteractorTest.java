package use_case.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.User;
import use_case.session.SessionRepository;

class LoginInteractorTest {

    private TestLoginDataAccess dataAccess;
    private TestLoginOutputBoundary outputBoundary;
    private TestSessionRepository sessionRepository;
    private LoginInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestLoginDataAccess();
        outputBoundary = new TestLoginOutputBoundary();
        sessionRepository = new TestSessionRepository();
        interactor = new LoginInteractor(dataAccess, outputBoundary, sessionRepository);
    }

    @Test
    void executeSuccessfulLogin() {
        // Arrange
        final User user = new User("John Doe", "johndoe", "john@example.com", "password123");
        dataAccess.addUser(user);
        final LoginInputData inputData = new LoginInputData("johndoe", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertFalse(outputBoundary.isFailCalled());
        assertNotNull(outputBoundary.getOutputData());

        // Verify InputData getters (covers LoginInputData)
        assertEquals("johndoe", inputData.getUsername());
        assertEquals("password123", inputData.getPassword());

        // Verify OutputData getters (covers LoginOutputData)
        assertEquals("johndoe", outputBoundary.getOutputData().getUsername());
        assertEquals("John Doe", outputBoundary.getOutputData().getFullName());
        assertFalse(outputBoundary.getOutputData().isUseCaseFailed());

        assertEquals(user, sessionRepository.getCurrentUser());
    }

    @Test
    void executeFailureEmptyUsername() {
        // Arrange
        final LoginInputData inputData = new LoginInputData("", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Username cannot be empty.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureNullUsername() {
        // Arrange
        final LoginInputData inputData = new LoginInputData(null, "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Username cannot be empty.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureWhitespaceUsername() {
        // Arrange
        final LoginInputData inputData = new LoginInputData("   ", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Username cannot be empty.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureEmptyPassword() {
        // Arrange
        final LoginInputData inputData = new LoginInputData("johndoe", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Password cannot be empty.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureNullPassword() {
        // Arrange
        final LoginInputData inputData = new LoginInputData("johndoe", null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Password cannot be empty.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureWhitespacePassword() {
        // Arrange
        final LoginInputData inputData = new LoginInputData("johndoe", "   ");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Password cannot be empty.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureUserNotFound() {
        // Arrange
        final LoginInputData inputData = new LoginInputData("nonexistent", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("User not found.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureIncorrectPassword() {
        // Arrange
        final User user = new User("John Doe", "johndoe", "john@example.com", "password123");
        dataAccess.addUser(user);
        final LoginInputData inputData = new LoginInputData("johndoe", "wrongpassword");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Incorrect password.", outputBoundary.getErrorMessage());
    }

    @Test
    void switchToSignupView() {
        // Act
        interactor.switchToSignupView();

        // Assert
        assertTrue(outputBoundary.isSwitchToSignupCalled());
    }

    // Test helper classes

    private static final class TestLoginDataAccess implements LoginDataAccessInterface {
        private final Map<String, User> usersByUsername = new HashMap<>();
        private final Map<String, User> usersByEmail = new HashMap<>();

        void addUser(User user) {
            usersByUsername.put(user.getUsername(), user);
            usersByEmail.put(user.getEmail(), user);
        }

        @Override
        public User getUserByUsername(String username) {
            return usersByUsername.get(username);
        }

        @Override
        public User getUserByEmail(String email) {
            return usersByEmail.get(email);
        }
    }

    private static final class TestLoginOutputBoundary implements LoginOutputBoundary {
        private boolean successCalled;
        private boolean failCalled;
        private boolean switchToSignupCalled;
        private LoginOutputData outputData;
        private String errorMessage;

        boolean isSuccessCalled() {
            return successCalled;
        }

        boolean isFailCalled() {
            return failCalled;
        }

        boolean isSwitchToSignupCalled() {
            return switchToSignupCalled;
        }

        LoginOutputData getOutputData() {
            return outputData;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public void prepareSuccessView(LoginOutputData data) {
            this.successCalled = true;
            this.outputData = data;
        }

        @Override
        public void prepareFailView(String message) {
            this.failCalled = true;
            this.errorMessage = message;
        }

        @Override
        public void switchToSignupView() {
            this.switchToSignupCalled = true;
        }
    }

    private static final class TestSessionRepository implements SessionRepository {
        private User currentUser;

        @Override
        public void setCurrentUser(User user) {
            this.currentUser = user;
        }

        @Override
        public User getCurrentUser() {
            return currentUser;
        }

        @Override
        public boolean isLoggedIn() {
            return currentUser != null;
        }

        @Override
        public void clearSession() {
            currentUser = null;
        }
    }
}
