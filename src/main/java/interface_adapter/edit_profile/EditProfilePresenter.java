package interface_adapter.edit_profile;

import interface_adapter.ViewManagerModel;
import use_case.edit_profile.EditProfileOutputBoundary;
import use_case.edit_profile.EditProfileOutputData;

/**
 * The Presenter for the Edit Profile Use Case.
 */
public class EditProfilePresenter implements EditProfileOutputBoundary {

    private final EditProfileViewModel editProfileViewModel;
    private final ViewManagerModel viewManagerModel;

    public EditProfilePresenter(EditProfileViewModel editProfileViewModel,
                               ViewManagerModel viewManagerModel) {
        this.editProfileViewModel = editProfileViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(EditProfileOutputData response) {
        // On success, update the state with the new information and clear errors
        final EditProfileState editProfileState = editProfileViewModel.getState();
        
        // Update the current username to reflect any username change
        editProfileState.setCurrentUsername(response.getUsername());
        editProfileState.setNewUsername(response.getUsername());
        editProfileState.setFullName(response.getFullName());
        editProfileState.setBio(response.getBio());
        editProfileState.setProfilePicture(response.getProfilePicture());
        
        // Clear password fields for security
        editProfileState.setCurrentPassword("");
        editProfileState.setNewPassword("");
        editProfileState.setRepeatNewPassword("");
        
        // Clear all errors
        editProfileState.setUsernameError(null);
        editProfileState.setFullNameError(null);
        editProfileState.setBioError(null);
        editProfileState.setProfilePictureError(null);
        editProfileState.setPasswordError(null);
        editProfileState.setGeneralError(null);

        editProfileViewModel.setState(editProfileState);
        editProfileViewModel.firePropertyChanged();

        // Optionally switch to profile view after successful edit
        // viewManagerModel.setState("profile");
        // viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final EditProfileState editProfileState = editProfileViewModel.getState();

        // Set the appropriate error based on the error message
        if (errorMessage.toLowerCase().contains("username")) {
            editProfileState.setUsernameError(errorMessage);
        } else if (errorMessage.toLowerCase().contains("full name")) {
            editProfileState.setFullNameError(errorMessage);
        } else if (errorMessage.toLowerCase().contains("bio")) {
            editProfileState.setBioError(errorMessage);
        } else if (errorMessage.toLowerCase().contains("password")) {
            editProfileState.setPasswordError(errorMessage);
        } else if (errorMessage.toLowerCase().contains("profile picture")) {
            editProfileState.setProfilePictureError(errorMessage);
        } else {
            // Generic error
            editProfileState.setGeneralError(errorMessage);
        }

        editProfileViewModel.firePropertyChanged();
    }
}
