package use_case.edit_profile;

import entities.User;

/**
 * Data access interface for the edit profile use case.
 */
public interface EditProfileDataAccessInterface {
    /**
     * Update the user's profile information.
     * @param username the username of the user whose profile is being updated
     * @param fullName the updated full name
     * @param bio the updated bio
     * @param profilePicture the updated profile picture URL/path
     */
    void updateUserProfile(String username, String fullName, String bio, String profilePicture);
}
