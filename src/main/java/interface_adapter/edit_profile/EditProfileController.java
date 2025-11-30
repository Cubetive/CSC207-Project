package interface_adapter.edit_profile;

import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInputData;

/**
 * Controller for the edit profile feature.
 * Handles user input from the view layer.
 */
public class EditProfileController {
    private final EditProfileInputBoundary editProfileInteractor;

    public EditProfileController(EditProfileInputBoundary editProfileInteractor) {
        this.editProfileInteractor = editProfileInteractor;
    }

    /**
     * Execute the edit profile use case.
     * @param currentUsername the current username of the user editing their profile
     * @param newUsername the new username (can be same as current if not changing)
     * @param fullName the updated full name
     * @param bio the updated bio
     * @param profilePicture the updated profile picture URL/path
     * @param currentPassword the current password (for verification if changing password)
     * @param newPassword the new password (null or empty if not changing)
     * @param repeatNewPassword the repeated new password (null or empty if not changing)
     */
    public void execute(String currentUsername, String newUsername, String fullName, 
                       String bio, String profilePicture, String currentPassword,
                       String newPassword, String repeatNewPassword) {
        final EditProfileInputData inputData = new EditProfileInputData(
            currentUsername, newUsername, fullName, bio, profilePicture,
            currentPassword, newPassword, repeatNewPassword
        );
        
        editProfileInteractor.execute(inputData);
    }
}
