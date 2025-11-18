package use_case.edit_profile;

/**
 * Input Boundary for actions related to editing a user's profile.
 */
public interface EditProfileInputBoundary {

    /**
     * Executes the edit profile use case.
     * @param editProfileInputData the input data for this use case
     */
    void execute(EditProfileInputData editProfileInputData);
}
