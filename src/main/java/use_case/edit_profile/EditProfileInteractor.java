package use_case.edit_profile;

import entities.User;
import use_case.session.SessionRepository;

/**
 * Interactor for the edit profile use case.
 * Contains the business logic for editing a user's profile.
 */
public class EditProfileInteractor implements EditProfileInputBoundary {
    private final EditProfileDataAccessInterface userDataAccess;
    private final EditProfileOutputBoundary userPresenter;
    private final SessionRepository sessionRepository;

    public EditProfileInteractor(EditProfileDataAccessInterface userDataAccess,
                                EditProfileOutputBoundary userPresenter,
                                SessionRepository sessionRepository) {
        this.userDataAccess = userDataAccess;
        this.userPresenter = userPresenter;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void execute(EditProfileInputData editProfileInputData) {
        // Validate that the user exists
        final User user = userDataAccess.getUserByUsername(editProfileInputData.getCurrentUsername());
        if (user == null) {
            userPresenter.prepareFailView("User not found.");
            return;
        }

        // Validate that full name is not empty
        if (editProfileInputData.getFullName() == null || editProfileInputData.getFullName().trim().isEmpty()) {
            userPresenter.prepareFailView("Full name cannot be empty.");
            return;
        }

        // Validate bio length (max 500 characters)
        if (editProfileInputData.getBio() != null && editProfileInputData.getBio().length() > 500) {
            userPresenter.prepareFailView("Bio cannot exceed 500 characters.");
            return;
        }

        // Handle username change if requested
        if (editProfileInputData.isChangingUsername()) {
            // Validate new username is not empty
            if (editProfileInputData.getNewUsername().trim().isEmpty()) {
                userPresenter.prepareFailView("Username cannot be empty.");
                return;
            }

            // Check if new username is already taken
            if (userDataAccess.existsByUsername(editProfileInputData.getNewUsername())) {
                userPresenter.prepareFailView("Username already exists. Please choose a different username.");
                return;
            }
        }

        // Handle password change if requested
        if (editProfileInputData.isChangingPassword()) {
            // Verify current password
            if (!user.getPassword().equals(editProfileInputData.getCurrentPassword())) {
                userPresenter.prepareFailView("Current password is incorrect.");
                return;
            }

            // Validate new passwords match
            if (!editProfileInputData.getNewPassword().equals(editProfileInputData.getRepeatNewPassword())) {
                userPresenter.prepareFailView("New passwords don't match.");
                return;
            }

            // Validate new password strength (at least 6 characters)
            if (editProfileInputData.getNewPassword().length() < 6) {
                userPresenter.prepareFailView("New password must be at least 6 characters long.");
                return;
            }

            // Update password
            userDataAccess.updatePassword(editProfileInputData.getCurrentUsername(),
                                         editProfileInputData.getNewPassword());
        }

        // Update profile information
        userDataAccess.updateUserProfile(
            editProfileInputData.getCurrentUsername(),
            editProfileInputData.getNewUsername(),
            editProfileInputData.getFullName(),
            editProfileInputData.getBio(),
            editProfileInputData.getProfilePicture()
        );

        // Update session if username changed
        if (editProfileInputData.isChangingUsername()) {
            final User updatedUser = userDataAccess.getUserByUsername(editProfileInputData.getNewUsername());
            if (updatedUser != null) {
                sessionRepository.setCurrentUser(updatedUser);
            }
        }

        // Prepare success response with updated information
        final EditProfileOutputData outputData = new EditProfileOutputData(
            editProfileInputData.getNewUsername(),
            editProfileInputData.getFullName(),
            editProfileInputData.getBio(),
            editProfileInputData.getProfilePicture(),
            false
        );

        userPresenter.prepareSuccessView(outputData);
    }
}
