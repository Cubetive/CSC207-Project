package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfilePresenter;
import interface_adapter.edit_profile.EditProfileViewModel;
import use_case.edit_profile.EditProfileDataAccessInterface;
import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInteractor;
import use_case.edit_profile.EditProfileOutputBoundary;

/**
 * Factory for creating the Edit Profile use case.
 */
public class EditProfileUseCaseFactory {

    /** Prevent instantiation. */
    private EditProfileUseCaseFactory() {
    }

    /**
     * Creates the Edit Profile controller.
     * @param viewManagerModel the view manager model
     * @param editProfileViewModel the edit profile view model
     * @param userDataAccessObject the data access object for user data
     * @return the edit profile controller
     */
    public static EditProfileController create(
            ViewManagerModel viewManagerModel,
            EditProfileViewModel editProfileViewModel,
            EditProfileDataAccessInterface userDataAccessObject) {

        final EditProfileOutputBoundary editProfileOutputBoundary = 
            new EditProfilePresenter(editProfileViewModel, viewManagerModel);

        final EditProfileInputBoundary editProfileInteractor = 
            new EditProfileInteractor(userDataAccessObject, editProfileOutputBoundary);

        return new EditProfileController(editProfileInteractor);
    }
}
