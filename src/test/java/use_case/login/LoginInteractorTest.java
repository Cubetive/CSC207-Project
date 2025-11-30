package use_case.login;

import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.session.SessionRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        User user = new User("John Doe", "johndoe", "john@example.com", "password123");
        dataAccess.addUser(user);
        LoginInputData inputData = new LoginInputData("johndoe", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertFalse(outputBoundary.failCalled);
        assertNotNull(outputBoundary.outputData);

        // Verify InputData getters (covers LoginInputData)
        assertEquals("johndoe", inputData.getUsername());
        assertEquals("password123", inputData.getPassword());

        // Verify OutputData getters (covers LoginOutputData)
        assertEquals("johndoe", outputBoundary.outputData.getUsername());
        assertEquals("John Doe", outputBoundary.outputData.getFullName());
        assertFalse(outputBoundary.outputData.isUseCaseFailed());

        assertEquals(user, sessionRepository.getCurrentUser());
    }

    @Test
    void executeFailureEmptyUsername() {
        // Arrange
        LoginInputData inputData = new LoginInputData("", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Username cannot be empty.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureNullUsername() {
        // Arrange
        LoginInputData inputData = new LoginInputData(null, "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Username cannot be empty.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureWhitespaceUsername() {
        // Arrange
        LoginInputData inputData = new LoginInputData("   ", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Username cannot be empty.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureEmptyPassword() {
        // Arrange
        LoginInputData inputData = new LoginInputData("johndoe", "");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Password cannot be empty.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureNullPassword() {
        // Arrange
        LoginInputData inputData = new LoginInputData("johndoe", null);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Password cannot be empty.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureWhitespacePassword() {
        // Arrange
        LoginInputData inputData = new LoginInputData("johndoe", "   ");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Password cannot be empty.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureUserNotFound() {
        // Arrange
        LoginInputData inputData = new LoginInputData("nonexistent", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("User not found.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureIncorrectPassword() {
        // Arrange
        User user = new User("John Doe", "johndoe", "john@example.com", "password123");
        dataAccess.addUser(user);
        LoginInputData inputData = new LoginInputData("johndoe", "wrongpassword");

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Incorrect password.", outputBoundary.errorMessage);
    }

    @Test
    void switchToSignupView() {
        // Act
        interactor.switchToSignupView();

        // Assert
        assertTrue(outputBoundary.switchToSignupCalled);
    }

    // Test helper classes

    private static class TestLoginDataAccess implements LoginDataAccessInterface {
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

    private static class TestLoginOutputBoundary implements LoginOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        boolean switchToSignupCalled = false;
        LoginOutputData outputData;
        String errorMessage;

        @Override
        public void prepareSuccessView(LoginOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failCalled = true;
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToSignupView() {
            this.switchToSignupCalled = true;
        }
    }

    private static class TestSessionRepository implements SessionRepository {
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
