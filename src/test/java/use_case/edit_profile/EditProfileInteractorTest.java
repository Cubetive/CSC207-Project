package use_case.edit_profile;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.User;
import use_case.session.SessionRepository;

class EditProfileInteractorTest {

    private TestEditProfileDataAccess dataAccess;
    private TestEditProfilePresenter presenter;
    private TestSessionRepository sessionRepository;
    private EditProfileInteractor interactor;
    private User existingUser;

    @BeforeEach
    void setUp() {
        dataAccess = new TestEditProfileDataAccess();
        presenter = new TestEditProfilePresenter();
        sessionRepository = new TestSessionRepository();
        interactor = new EditProfileInteractor(dataAccess, presenter, sessionRepository);

        existingUser = new User("Existing User", "existing_user", "existing@example.com", "currentPass!");
        dataAccess.addUser(existingUser);
    }

    @Test
    void executeSuccessfulProfileAndPasswordChange() {
        final EditProfileInputData inputData = new EditProfileInputData(
                "existing_user",
                "new_user",
                "Updated Name",
                "Updated bio",
                "picture.png",
                "currentPass!",
                "newPass123",
                "newPass123"
        );

        interactor.execute(inputData);

        assertTrue(presenter.isSuccessCalled());
        assertFalse(presenter.isFailCalled());
        assertNotNull(presenter.getOutputData());
        assertEquals("new_user", presenter.getOutputData().getUsername());
        assertEquals("Updated Name", presenter.getOutputData().getFullName());
        assertEquals("Updated bio", presenter.getOutputData().getBio());
        assertEquals("picture.png", presenter.getOutputData().getProfilePicture());
        assertFalse(presenter.getOutputData().isUseCaseFailed());

        assertEquals("new_user", sessionRepository.getCurrentUser().getUsername());
        assertEquals("existing_user", dataAccess.getUpdatedProfileCurrentUsername());
        assertEquals("new_user", dataAccess.getUpdatedProfileNewUsername());
        assertEquals("newPass123", dataAccess.getUpdatedPasswordValue());
    }

    @Test
    void inputDataChangeFlagsCoverGetters() {
        final EditProfileInputData noChangeData = new EditProfileInputData(
                "same_user", "same_user", "Name", "Bio", null, null, null, null
        );
        assertEquals("same_user", noChangeData.getCurrentUsername());
        assertEquals("same_user", noChangeData.getNewUsername());
        assertEquals("Name", noChangeData.getFullName());
        assertEquals("Bio", noChangeData.getBio());
        assertNull(noChangeData.getProfilePicture());
        assertNull(noChangeData.getCurrentPassword());
        assertNull(noChangeData.getNewPassword());
        assertNull(noChangeData.getRepeatNewPassword());
        assertFalse(noChangeData.isChangingUsername());
        assertFalse(noChangeData.isChangingPassword());

        final EditProfileInputData changeData = new EditProfileInputData(
                "old", "new", "Name", "Bio", "", "oldPass", "newPass", "newPass"
        );
        assertTrue(changeData.isChangingUsername());
        assertTrue(changeData.isChangingPassword());
    }

    @Test
    void executeFailureUserNotFound() {
        final EditProfileInputData inputData = new EditProfileInputData(
                "unknown", "unknown", "Name", "Bio", null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("User not found.");
    }

    @Test
    void executeFailureEmptyFullName() {
        final EditProfileInputData inputData = new EditProfileInputData(
                "existing_user", "existing_user", "", "Bio", null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("Full name cannot be empty.");
    }

    @Test
    void executeFailureBioTooLong() {
        final String longBio = "a".repeat(501);
        final EditProfileInputData inputData = new EditProfileInputData(
                "existing_user", "existing_user", "Name", longBio, null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("Bio cannot exceed 500 characters.");
    }

    @Test
    void executeFailureUsernameEmptyWhenChanging() {
        final EditProfileInputData inputData = new EditProfileInputData(
                "existing_user", "", "Name", "Bio", null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("Username cannot be empty.");
    }

    @Test
    void executeFailureUsernameAlreadyExists() {
        final User otherUser = new User("Other", "taken_user", "other@example.com", "pass");
        dataAccess.addUser(otherUser);

        final EditProfileInputData inputData = new EditProfileInputData(
                "existing_user", "taken_user", "Name", "Bio", null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("Username already exists. Please choose a different username.");
    }

    @Test
    void executeFailureCurrentPasswordIncorrect() {
        final EditProfileInputData inputData = new EditProfileInputData(
                "existing_user", "existing_user", "Name", "Bio", null,
                "wrongPass", "newPass123", "newPass123"
        );

        interactor.execute(inputData);

        assertFailWithMessage("Current password is incorrect.");
    }

    @Test
    void executeFailureNewPasswordsMismatch() {
        final EditProfileInputData inputData = new EditProfileInputData(
                "existing_user", "existing_user", "Name", "Bio", null,
                "currentPass!", "newPass123", "different"
        );

        interactor.execute(inputData);

        assertFailWithMessage("New passwords don't match.");
    }

    @Test
    void executeFailureNewPasswordTooShort() {
        final EditProfileInputData inputData = new EditProfileInputData(
                "existing_user", "existing_user", "Name", "Bio", null,
                "currentPass!", "short", "short"
        );

        interactor.execute(inputData);

        assertFailWithMessage("New password must be at least 6 characters long.");
    }

    private void assertFailWithMessage(final String expectedMessage) {
        assertFalse(presenter.isSuccessCalled());
        assertTrue(presenter.isFailCalled());
        assertEquals(expectedMessage, presenter.getErrorMessage());
    }

    private static final class TestEditProfileDataAccess implements EditProfileDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private String updatedProfileCurrentUsername;
        private String updatedProfileNewUsername;
        private String updatedPasswordUsername;
        private String updatedPasswordValue;

        void addUser(final User user) {
            users.put(user.getUsername(), user);
        }

        @Override
        public boolean existsByUsername(final String username) {
            return users.containsKey(username);
        }

        @Override
        public User getUserByUsername(final String username) {
            return users.get(username);
        }

        @Override
        public void updateUserProfile(final String currentUsername,
                                      final String newUsername,
                                      final String fullName,
                                      final String bio,
                                      final String profilePicture) {
            this.updatedProfileCurrentUsername = currentUsername;
            this.updatedProfileNewUsername = newUsername;

            final User user = users.remove(currentUsername);
            if (user != null) {
                user.setUsername(newUsername);
                user.setFullName(fullName);
                user.setBio(bio);
                user.setProfilePicture(profilePicture);
                users.put(newUsername, user);
            }
        }

        @Override
        public void updatePassword(final String username, final String newPassword) {
            this.updatedPasswordUsername = username;
            this.updatedPasswordValue = newPassword;

            final User user = users.get(username);
            if (user != null) {
                user.setPassword(newPassword);
            }
        }

        public Map<String, User> getUsers() {
            return users;
        }

        public String getUpdatedProfileCurrentUsername() {
            return updatedProfileCurrentUsername;
        }

        public String getUpdatedProfileNewUsername() {
            return updatedProfileNewUsername;
        }

        public String getUpdatedPasswordUsername() {
            return updatedPasswordUsername;
        }

        public String getUpdatedPasswordValue() {
            return updatedPasswordValue;
        }
    }

    private static final class TestEditProfilePresenter implements EditProfileOutputBoundary {
        private boolean successCalled;
        private boolean failCalled;
        private EditProfileOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(final EditProfileOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(final String errorMessage) {
            this.failCalled = true;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccessCalled() {
            return successCalled;
        }

        public boolean isFailCalled() {
            return failCalled;
        }

        public EditProfileOutputData getOutputData() {
            return outputData;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    private static final class TestSessionRepository implements SessionRepository {
        private User currentUser;

        @Override
        public void setCurrentUser(final User user) {
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
