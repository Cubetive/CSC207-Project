package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.edit_profile.EditProfileController;
import interface_adapter.edit_profile.EditProfilePresenter;
import interface_adapter.edit_profile.EditProfileViewModel;
import use_case.edit_profile.EditProfileDataAccessInterface;
import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInteractor;
import use_case.edit_profile.EditProfileOutputBoundary;
import use_case.session.SessionRepository;

/**
 * Factory for creating the Edit Profile use case.
 */
public final class EditProfileUseCaseFactory {

    /** Prevent instantiation. */
    private EditProfileUseCaseFactory() {
    }

    /**
     * Creates the Edit Profile controller.
     * @param viewManagerModel the view manager model
     * @param editProfileViewModel the edit profile view model
     * @param userDataAccessObject the data access object for user data
     * @param sessionRepository the session repository for updating the session
     * @return the edit profile controller
     */
    public static EditProfileController create(
            ViewManagerModel viewManagerModel,
            EditProfileViewModel editProfileViewModel,
            EditProfileDataAccessInterface userDataAccessObject,
            SessionRepository sessionRepository) {

        final EditProfileOutputBoundary editProfileOutputBoundary = 
            new EditProfilePresenter(editProfileViewModel, viewManagerModel);

        final EditProfileInputBoundary editProfileInteractor = 
            new EditProfileInteractor(userDataAccessObject, editProfileOutputBoundary, sessionRepository);

        return new EditProfileController(editProfileInteractor);
    }
}
