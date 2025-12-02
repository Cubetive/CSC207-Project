package use_case.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.User;
import entities.UserFactory;
import use_case.session.SessionRepository;

class SignupInteractorTest {

    private TestSignupDataAccess dataAccess;
    private TestSignupOutputBoundary outputBoundary;
    private TestUserFactory userFactory;
    private TestSessionRepository sessionRepository;
    private SignupInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestSignupDataAccess();
        outputBoundary = new TestSignupOutputBoundary();
        userFactory = new TestUserFactory();
        sessionRepository = new TestSessionRepository();
        interactor = new SignupInteractor(dataAccess, outputBoundary, userFactory, sessionRepository);
    }

    @Test
    void executeSuccessfulSignup() {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertFalse(outputBoundary.isFailCalled());
        assertNotNull(outputBoundary.getOutputData());

        // Verify InputData getters (covers SignupInputData)
        assertEquals("John Doe", inputData.getFullName());
        assertEquals("johndoe", inputData.getUsername());
        assertEquals("john@example.com", inputData.getEmail());
        assertEquals("password123", inputData.getPassword());
        assertEquals("password123", inputData.getRepeatPassword());

        // Verify OutputData getters (covers SignupOutputData)
        assertEquals("johndoe", outputBoundary.getOutputData().getUsername());
        assertEquals("John Doe", outputBoundary.getOutputData().getFullName());
        assertFalse(outputBoundary.getOutputData().isUseCaseFailed());

        assertTrue(dataAccess.isUserSaved());
        assertNotNull(sessionRepository.getCurrentUser());
    }

    @Test
    void executeFailureUsernameExists() {
        // Arrange
        dataAccess.addExistingUsername("johndoe");
        final SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Username already exists.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureEmailExists() {
        // Arrange
        dataAccess.addExistingEmail("john@example.com");
        final SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Email already exists.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailurePasswordsDoNotMatch() {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "password123", "differentpassword"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Passwords don't match.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailurePasswordTooShort() {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "pass", "pass"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Password must be at least 6 characters long.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureInvalidEmailFormat() {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "invalidemail", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Invalid email format.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureInvalidEmailFormatNoToplevelDomain() {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Invalid email format.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureEmptyFullName() {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
                "   ", "johndoe", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Full name cannot be empty.", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureEmptyUsername() {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
                "John Doe", "   ", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Username cannot be empty.", outputBoundary.getErrorMessage());
    }

    @Test
    void switchToLoginView() {
        // Act
        interactor.switchToLoginView();

        // Assert
        assertTrue(outputBoundary.isSwitchToLoginCalled());
    }

    @Test
    void executeSuccessfulSignupWithValidEmailFormats() {
        // Test various valid email formats
        final String[] validEmails = {
            "test@example.com",
            "test.name@example.com",
            "test+tag@example.com",
            "test_name@example.org",
            "test123@example.co.uk",
        };

        for (String email : validEmails) {
            // Reset for each test
            setUp();
            final SignupInputData inputData = new SignupInputData(
                    "Test User", "testuser", email, "password123", "password123"
            );

            interactor.execute(inputData);

            assertTrue(outputBoundary.isSuccessCalled(), "Should succeed with email: " + email);
        }
    }

    // Test helper classes

    private static final class TestSignupDataAccess implements SignupDataAccessInterface {
        private final Set<String> existingUsernames = new HashSet<>();
        private final Set<String> existingEmails = new HashSet<>();
        private boolean userSaved;

        void addExistingUsername(String username) {
            existingUsernames.add(username);
        }

        void addExistingEmail(String email) {
            existingEmails.add(email);
        }

        boolean isUserSaved() {
            return userSaved;
        }

        @Override
        public boolean existsByUsername(String username) {
            return existingUsernames.contains(username);
        }

        @Override
        public boolean existsByEmail(String email) {
            return existingEmails.contains(email);
        }

        @Override
        public void save(User user) {
            userSaved = true;
            existingUsernames.add(user.getUsername());
            existingEmails.add(user.getEmail());
        }
    }

    private static final class TestSignupOutputBoundary implements SignupOutputBoundary {
        private boolean successCalled;
        private boolean failCalled;
        private boolean switchToLoginCalled;
        private SignupOutputData outputData;
        private String errorMessage;

        boolean isSuccessCalled() {
            return successCalled;
        }

        boolean isFailCalled() {
            return failCalled;
        }

        boolean isSwitchToLoginCalled() {
            return switchToLoginCalled;
        }

        SignupOutputData getOutputData() {
            return outputData;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public void prepareSuccessView(SignupOutputData data) {
            this.successCalled = true;
            this.outputData = data;
        }

        @Override
        public void prepareFailView(String message) {
            this.failCalled = true;
            this.errorMessage = message;
        }

        @Override
        public void switchToLoginView() {
            this.switchToLoginCalled = true;
        }
    }

    private static final class TestUserFactory implements UserFactory {
        @Override
        public User create(String fullName, String username, String email, String password) {
            return new User(fullName, username, email, password);
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
