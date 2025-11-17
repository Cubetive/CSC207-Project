package use_case.edit_profile;

// import entities.User;

/**
 * Interactor for the edit profile use case.
 * Contains the business logic for editing a user's profile.
 */
public class EditProfileInteractor {
    private final EditProfileDataAccessInterface userDataAccess;

    public EditProfileInteractor(EditProfileDataAccessInterface userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public void execute(String username, String fullName, String bio, String profilePicture) {
        // TODO: Implement validation logic
        // TODO: Validate input data (e.g., bio length, profile picture URL format)

        userDataAccess.updateUserProfile(username, fullName, bio, profilePicture);
    }
}
