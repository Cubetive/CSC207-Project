package use_case.edit_profile;

import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.session.SessionRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        EditProfileInputData inputData = new EditProfileInputData(
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

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertNotNull(presenter.outputData);
        assertEquals("new_user", presenter.outputData.getUsername());
        assertEquals("Updated Name", presenter.outputData.getFullName());
        assertEquals("Updated bio", presenter.outputData.getBio());
        assertEquals("picture.png", presenter.outputData.getProfilePicture());
        assertFalse(presenter.outputData.isUseCaseFailed());

        assertEquals("new_user", sessionRepository.getCurrentUser().getUsername());
        assertEquals("newPass123", dataAccess.updatedPasswordValue);
        assertEquals("existing_user", dataAccess.updatedProfileCurrentUsername);
        assertEquals("new_user", dataAccess.updatedProfileNewUsername);
    }

    @Test
    void inputDataChangeFlagsCoverGetters() {
        EditProfileInputData noChangeData = new EditProfileInputData(
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

        EditProfileInputData changeData = new EditProfileInputData(
            "old", "new", "Name", "Bio", "", "oldPass", "newPass", "newPass"
        );
        assertTrue(changeData.isChangingUsername());
        assertTrue(changeData.isChangingPassword());
    }

    @Test
    void executeFailureUserNotFound() {
        EditProfileInputData inputData = new EditProfileInputData(
            "unknown", "unknown", "Name", "Bio", null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("User not found.");
    }

    @Test
    void executeFailureEmptyFullName() {
        EditProfileInputData inputData = new EditProfileInputData(
            "existing_user", "existing_user", "", "Bio", null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("Full name cannot be empty.");
    }

    @Test
    void executeFailureBioTooLong() {
        String longBio = "a".repeat(501);
        EditProfileInputData inputData = new EditProfileInputData(
            "existing_user", "existing_user", "Name", longBio, null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("Bio cannot exceed 500 characters.");
    }

    @Test
    void executeFailureUsernameEmptyWhenChanging() {
        EditProfileInputData inputData = new EditProfileInputData(
            "existing_user", "", "Name", "Bio", null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("Username cannot be empty.");
    }

    @Test
    void executeFailureUsernameAlreadyExists() {
        User otherUser = new User("Other", "taken_user", "other@example.com", "pass");
        dataAccess.addUser(otherUser);

        EditProfileInputData inputData = new EditProfileInputData(
            "existing_user", "taken_user", "Name", "Bio", null, null, null, null
        );

        interactor.execute(inputData);

        assertFailWithMessage("Username already exists. Please choose a different username.");
    }

    @Test
    void executeFailureCurrentPasswordIncorrect() {
        EditProfileInputData inputData = new EditProfileInputData(
            "existing_user", "existing_user", "Name", "Bio", null,
            "wrongPass", "newPass123", "newPass123"
        );

        interactor.execute(inputData);

        assertFailWithMessage("Current password is incorrect.");
    }

    @Test
    void executeFailureNewPasswordsMismatch() {
        EditProfileInputData inputData = new EditProfileInputData(
            "existing_user", "existing_user", "Name", "Bio", null,
            "currentPass!", "newPass123", "different"
        );

        interactor.execute(inputData);

        assertFailWithMessage("New passwords don't match.");
    }

    @Test
    void executeFailureNewPasswordTooShort() {
        EditProfileInputData inputData = new EditProfileInputData(
            "existing_user", "existing_user", "Name", "Bio", null,
            "currentPass!", "short", "short"
        );

        interactor.execute(inputData);

        assertFailWithMessage("New password must be at least 6 characters long.");
    }

    private void assertFailWithMessage(String expectedMessage) {
        assertFalse(presenter.successCalled);
        assertTrue(presenter.failCalled);
        assertEquals(expectedMessage, presenter.errorMessage);
    }

    private static class TestEditProfileDataAccess implements EditProfileDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private String updatedProfileCurrentUsername;
        private String updatedProfileNewUsername;
        private String updatedPasswordUsername;
        private String updatedPasswordValue;

        void addUser(User user) {
            users.put(user.getUsername(), user);
        }

        @Override
        public boolean existsByUsername(String username) {
            return users.containsKey(username);
        }

        @Override
        public User getUserByUsername(String username) {
            return users.get(username);
        }

        @Override
        public void updateUserProfile(String currentUsername, String newUsername, String fullName,
                                      String bio, String profilePicture) {
            this.updatedProfileCurrentUsername = currentUsername;
            this.updatedProfileNewUsername = newUsername;

            User user = users.remove(currentUsername);
            if (user != null) {
                user.setUsername(newUsername);
                user.setFullName(fullName);
                user.setBio(bio);
                user.setProfilePicture(profilePicture);
                users.put(newUsername, user);
            }
        }

        @Override
        public void updatePassword(String username, String newPassword) {
            this.updatedPasswordUsername = username;
            this.updatedPasswordValue = newPassword;
            User user = users.get(username);
            if (user != null) {
                user.setPassword(newPassword);
            }
        }
    }

    private static class TestEditProfilePresenter implements EditProfileOutputBoundary {
        boolean successCalled;
        boolean failCalled;
        EditProfileOutputData outputData;
        String errorMessage;

        @Override
        public void prepareSuccessView(EditProfileOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failCalled = true;
            this.errorMessage = errorMessage;
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

