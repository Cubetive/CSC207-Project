package data_access;

import entities.User;
import use_case.edit_profile.EditProfileDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data.
 * This implementation uses HashMaps to store users by username and email.
 */
public class InMemoryUserDataAccessObject implements EditProfileDataAccessInterface {

    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();

    /**
     * Gets a user by username.
     * @param username the username to search for
     * @return the user with the given username, or null if not found
     */
    public User getUserByUsername(String username) {
        return usersByUsername.get(username);
    }

    /**
     * Gets a user by email.
     * @param email the email to search for
     * @return the user with the given email, or null if not found
     */
    public User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    /**
     * Gets all users.
     * @return a map of all users by username
     */
    public Map<String, User> getAllUsers() {
        return new HashMap<>(usersByUsername);
    }

    /**
     * Clears all users from storage.
     * Useful for testing.
     */
    public void clear() {
        usersByUsername.clear();
        usersByEmail.clear();
    }

    /**
     * Updates a user's profile information.
     * @param username the username of the user to update
     * @param fullName the new full name
     * @param bio the new bio
     * @param profilePicture the new profile picture URL
     */
    public void updateUserProfile(String username, String fullName, String bio, String profilePicture) {
        User user = usersByUsername.get(username);
        if (user != null) {
            user.setFullName(fullName);
            user.setBio(bio);
            user.setProfilePicture(profilePicture);
        }
    }
}
