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
     * Check if a user with the given username exists.
     * @param username the username to check
     * @return true if a user with the given username exists, false otherwise
     */
    @Override
    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username);
    }

    /**
     * Gets a user by username.
     * @param username the username to search for
     * @return the user with the given username, or null if not found
     */
    @Override
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
     * @param currentUsername the current username of the user to update
     * @param newUsername the new username (can be same as current if not changing)
     * @param fullName the new full name
     * @param bio the new bio
     * @param profilePicture the new profile picture URL
     */
    @Override
    public void updateUserProfile(String currentUsername, String newUsername, String fullName, 
                                  String bio, String profilePicture) {
        User user = usersByUsername.get(currentUsername);
        if (user != null) {
            // Update the user's profile information
            user.editProfile(fullName, bio, profilePicture);
            
            // If username is changing, update the maps
            if (!currentUsername.equals(newUsername)) {
                user.setUsername(newUsername);
                usersByUsername.remove(currentUsername);
                usersByUsername.put(newUsername, user);
                // Email map doesn't need updating since email didn't change
            }
        }
    }

    /**
     * Updates a user's password.
     * @param username the username of the user
     * @param newPassword the new password
     */
    @Override
    public void updatePassword(String username, String newPassword) {
        User user = usersByUsername.get(username);
        if (user != null) {
            user.setPassword(newPassword);
        }
    }
}
