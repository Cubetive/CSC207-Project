package interface_adapter.edit_profile;

import use_case.edit_profile.EditProfileInteractor;

/**
 * Controller for the edit profile feature.
 * Handles user input from the view layer.
 */
public class EditProfileController {
    private final EditProfileInteractor editProfileInteractor;

    public EditProfileController(EditProfileInteractor editProfileInteractor) {
        this.editProfileInteractor = editProfileInteractor;
    }

    /**
     * Execute the edit profile use case.
     * @param username the username of the user editing their profile
     * @param fullName the updated full name
     * @param bio the updated bio
     * @param profilePicture the updated profile picture URL/path
     */
    public void execute(String username, String fullName, String bio, String profilePicture) {
        editProfileInteractor.execute(username, fullName, bio, profilePicture);
    }
}
