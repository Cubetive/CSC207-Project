package use_case.signup;

import entities.User;
import entities.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.session.SessionRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
        SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertFalse(outputBoundary.failCalled);
        assertNotNull(outputBoundary.outputData);

        // Verify InputData getters (covers SignupInputData)
        assertEquals("John Doe", inputData.getFullName());
        assertEquals("johndoe", inputData.getUsername());
        assertEquals("john@example.com", inputData.getEmail());
        assertEquals("password123", inputData.getPassword());
        assertEquals("password123", inputData.getRepeatPassword());

        // Verify OutputData getters (covers SignupOutputData)
        assertEquals("johndoe", outputBoundary.outputData.getUsername());
        assertEquals("John Doe", outputBoundary.outputData.getFullName());
        assertFalse(outputBoundary.outputData.isUseCaseFailed());

        assertTrue(dataAccess.userSaved);
        assertNotNull(sessionRepository.getCurrentUser());
    }

    @Test
    void executeFailureUsernameExists() {
        // Arrange
        dataAccess.existingUsernames.add("johndoe");
        SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Username already exists.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureEmailExists() {
        // Arrange
        dataAccess.existingEmails.add("john@example.com");
        SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Email already exists.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailurePasswordsDoNotMatch() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "password123", "differentpassword"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Passwords don't match.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailurePasswordTooShort() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example.com", "pass", "pass"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Password must be at least 6 characters long.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureInvalidEmailFormat() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "invalidemail", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Invalid email format.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureInvalidEmailFormatNoTLD() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
                "John Doe", "johndoe", "john@example", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Invalid email format.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureEmptyFullName() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
                "   ", "johndoe", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Full name cannot be empty.", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureEmptyUsername() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
                "John Doe", "   ", "john@example.com", "password123", "password123"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Username cannot be empty.", outputBoundary.errorMessage);
    }

    @Test
    void switchToLoginView() {
        // Act
        interactor.switchToLoginView();

        // Assert
        assertTrue(outputBoundary.switchToLoginCalled);
    }

    @Test
    void executeSuccessfulSignupWithValidEmailFormats() {
        // Test various valid email formats
        String[] validEmails = {
                "test@example.com",
                "test.name@example.com",
                "test+tag@example.com",
                "test_name@example.org",
                "test123@example.co.uk"
        };

        for (String email : validEmails) {
            setUp(); // Reset for each test
            SignupInputData inputData = new SignupInputData(
                    "Test User", "testuser", email, "password123", "password123"
            );

            interactor.execute(inputData);

            assertTrue(outputBoundary.successCalled, "Should succeed with email: " + email);
        }
    }

    // Test helper classes

    private static class TestSignupDataAccess implements SignupDataAccessInterface {
        Set<String> existingUsernames = new HashSet<>();
        Set<String> existingEmails = new HashSet<>();
        boolean userSaved = false;

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

    private static class TestSignupOutputBoundary implements SignupOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        boolean switchToLoginCalled = false;
        SignupOutputData outputData;
        String errorMessage;

        @Override
        public void prepareSuccessView(SignupOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failCalled = true;
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToLoginView() {
            this.switchToLoginCalled = true;
        }
    }

    private static class TestUserFactory implements UserFactory {
        @Override
        public User create(String fullName, String username, String email, String password) {
            return new User(fullName, username, email, password);
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
