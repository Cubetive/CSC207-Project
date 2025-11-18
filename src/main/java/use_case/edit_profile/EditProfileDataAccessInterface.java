package use_case.edit_profile;

import entities.User;

/**
 * Data access interface for the edit profile use case.
 */
public interface EditProfileDataAccessInterface {
    /**
     * Check if a user with the given username exists.
     * @param username the username to check
     * @return true if a user with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Get a user by their username.
     * @param username the username to search for
     * @return the user with the given username, or null if not found
     */
    User getUserByUsername(String username);

    /**
     * Update the user's profile information.
     * @param currentUsername the current username of the user
     * @param newUsername the new username (can be same as current if not changing)
     * @param fullName the updated full name
     * @param bio the updated bio
     * @param profilePicture the updated profile picture URL/path
     */
    void updateUserProfile(String currentUsername, String newUsername, String fullName, 
                          String bio, String profilePicture);

    /**
     * Update the user's password.
     * @param username the username of the user
     * @param newPassword the new password
     */
    void updatePassword(String username, String newPassword);
}
