package use_case.edit_profile;

import data_access.InMemorySessionRepository;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditProfileInteractor.
 */
public class EditProfileInteractorTest {

    private TestEditProfileDataAccess userDataAccess;
    private InMemorySessionRepository sessionRepository;
    private EditProfileInteractor interactor;

    @BeforeEach
    void setUp() {
        userDataAccess = new TestEditProfileDataAccess();
        sessionRepository = new InMemorySessionRepository();
    }

    // ========== Success Tests ==========

    @Test
    void successEditProfileWithoutChangesTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "password123");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe Updated", "New bio", "newpic.jpg",
            null, null, null
        );

        EditProfileOutputBoundary successPresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertEquals("testuser", outputData.getUsername());
                assertEquals("John Doe Updated", outputData.getFullName());
                assertEquals("New bio", outputData.getBio());
                assertEquals("newpic.jpg", outputData.getProfilePicture());
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Should succeed, but got error: " + errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, successPresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void successEditProfileWithUsernameChangeTest() {
        User user = new User("John Doe", "olduser", "test@example.com", "password123");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "olduser", "newuser", "John Doe", "Bio", null,
            null, null, null
        );

        EditProfileOutputBoundary successPresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertEquals("newuser", outputData.getUsername());
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Should succeed, but got error: " + errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, successPresenter, sessionRepository);
        interactor.execute(inputData);
        
        // Verify username was changed
        assertNull(userDataAccess.getUserByUsername("olduser"));
        assertNotNull(userDataAccess.getUserByUsername("newuser"));
    }

    @Test
    void successEditProfileWithPasswordChangeTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "oldpassword");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe", null, null,
            "oldpassword", "newpassword123", "newpassword123"
        );

        EditProfileOutputBoundary successPresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Should succeed, but got error: " + errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, successPresenter, sessionRepository);
        interactor.execute(inputData);
        
        // Verify password was changed
        User updatedUser = userDataAccess.getUserByUsername("testuser");
        assertEquals("newpassword123", updatedUser.getPassword());
    }

    @Test
    void successEditProfileWithAllChangesTest() {
        User user = new User("Old Name", "olduser", "test@example.com", "oldpass");
        userDataAccess.addUser(user);
        sessionRepository.setCurrentUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "olduser", "newuser", "New Name", "New Bio", "newpic.jpg",
            "oldpass", "newpass123", "newpass123"
        );

        EditProfileOutputBoundary successPresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertEquals("newuser", outputData.getUsername());
                assertEquals("New Name", outputData.getFullName());
                assertEquals("New Bio", outputData.getBio());
                assertEquals("newpic.jpg", outputData.getProfilePicture());
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Should succeed, but got error: " + errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, successPresenter, sessionRepository);
        interactor.execute(inputData);
        
        // Verify session was updated
        User sessionUser = sessionRepository.getCurrentUser();
        assertNotNull(sessionUser);
        assertEquals("newuser", sessionUser.getUsername());
    }

    // ========== Failure Tests ==========

    @Test
    void failUserNotFoundTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "nonexistent", "nonexistent", "Name", null, null,
            null, null, null
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail when user not found.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User not found.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failEmptyFullNameTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "password");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "", null, null,
            null, null, null
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with empty full name.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Full name cannot be empty.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failNullFullNameTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "password");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", null, null, null,
            null, null, null
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with null full name.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Full name cannot be empty.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failWhitespaceOnlyFullNameTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "password");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "   ", null, null,
            null, null, null
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with whitespace-only full name.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Full name cannot be empty.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failBioTooLongTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "password");
        userDataAccess.addUser(user);

        String longBio = "a".repeat(501); // 501 characters

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe", longBio, null,
            null, null, null
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with bio too long.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Bio cannot exceed 500 characters.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failBioExactly500CharactersTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "password");
        userDataAccess.addUser(user);

        String bio500 = "a".repeat(500); // Exactly 500 characters

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe", bio500, null,
            null, null, null
        );

        EditProfileOutputBoundary successPresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertEquals(500, outputData.getBio().length());
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Should succeed with exactly 500 characters: " + errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, successPresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failEmptyNewUsernameTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "password");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "", "John Doe", null, null,
            null, null, null
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with empty new username.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Username cannot be empty.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failWhitespaceOnlyNewUsernameTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "password");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "   ", "John Doe", null, null,
            null, null, null
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with whitespace-only new username.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Username cannot be empty.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failUsernameAlreadyExistsTest() {
        User user1 = new User("John Doe", "user1", "test1@example.com", "password");
        User user2 = new User("Jane Doe", "user2", "test2@example.com", "password");
        userDataAccess.addUser(user1);
        userDataAccess.addUser(user2);

        EditProfileInputData inputData = new EditProfileInputData(
            "user1", "user2", "John Doe", null, null,
            null, null, null
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail when username already exists.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Username already exists. Please choose a different username.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failIncorrectCurrentPasswordTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "correctpass");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe", null, null,
            "wrongpass", "newpass123", "newpass123"
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with incorrect current password.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Current password is incorrect.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failPasswordsDontMatchTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "oldpass");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe", null, null,
            "oldpass", "newpass123", "differentpass"
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail when passwords don't match.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("New passwords don't match.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failPasswordTooShortTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "oldpass");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe", null, null,
            "oldpass", "short", "short"
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with password too short.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("New password must be at least 6 characters long.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failPasswordExactly5CharactersTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "oldpass");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe", null, null,
            "oldpass", "12345", "12345"
        );

        EditProfileOutputBoundary failurePresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                fail("Should fail with password exactly 5 characters.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("New password must be at least 6 characters long.", errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void successPasswordExactly6CharactersTest() {
        User user = new User("John Doe", "testuser", "test@example.com", "oldpass");
        userDataAccess.addUser(user);

        EditProfileInputData inputData = new EditProfileInputData(
            "testuser", "testuser", "John Doe", null, null,
            "oldpass", "123456", "123456"
        );

        EditProfileOutputBoundary successPresenter = new EditProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(EditProfileOutputData outputData) {
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Should succeed with exactly 6 characters: " + errorMessage);
            }
        };

        interactor = new EditProfileInteractor(userDataAccess, successPresenter, sessionRepository);
        interactor.execute(inputData);
    }

    /**
     * Test implementation of EditProfileDataAccessInterface for testing.
     * Extends the pattern used in other tests by providing a way to add users.
     */
    private static class TestEditProfileDataAccess implements EditProfileDataAccessInterface {
        private final java.util.Map<String, User> users = new java.util.HashMap<>();

        public void addUser(User user) {
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
            User user = users.get(currentUsername);
            if (user != null) {
                user.editProfile(fullName, bio, profilePicture);
                if (!currentUsername.equals(newUsername)) {
                    user.setUsername(newUsername);
                    users.remove(currentUsername);
                    users.put(newUsername, user);
                }
            }
        }

        @Override
        public void updatePassword(String username, String newPassword) {
            User user = users.get(username);
            if (user != null) {
                user.setPassword(newPassword);
            }
        }
    }
}

