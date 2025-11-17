package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

/**
 * Controller for the Signup Use Case.
 */
public class SignupController {

    private final SignupInputBoundary signupUseCaseInteractor;

    public SignupController(SignupInputBoundary signupUseCaseInteractor) {
        this.signupUseCaseInteractor = signupUseCaseInteractor;
    }

    /**
     * Executes the Signup Use Case.
     * @param fullName the full name of the user signing up
     * @param username the username of the user signing up
     * @param email the email of the user signing up
     * @param password the password
     * @param repeatPassword the password repeated
     */
    public void execute(String fullName, String username, String email,
                       String password, String repeatPassword) {
        final SignupInputData signupInputData = new SignupInputData(
                fullName, username, email, password, repeatPassword);

        signupUseCaseInteractor.execute(signupInputData);
    }

    /**
     * Executes the "switch to LoginView" Use Case.
     */
    public void switchToLoginView() {
        signupUseCaseInteractor.switchToLoginView();
    }
}
