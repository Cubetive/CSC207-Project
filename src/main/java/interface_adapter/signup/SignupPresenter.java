package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * The Presenter for the Signup Use Case.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;

    public SignupPresenter(SignupViewModel signupViewModel,
                          ViewManagerModel viewManagerModel) {
        this.signupViewModel = signupViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData response) {
        // On success, we clear the signup form and switch to login view
        final SignupState signupState = signupViewModel.getState();
        signupState.setFullName("");
        signupState.setUsername("");
        signupState.setEmail("");
        signupState.setPassword("");
        signupState.setRepeatPassword("");
        signupState.setFullNameError(null);
        signupState.setUsernameError(null);
        signupState.setEmailError(null);
        signupState.setPasswordError(null);
        signupState.setRepeatPasswordError(null);

        signupViewModel.setState(signupState);
        signupViewModel.firePropertyChanged();

        // Switch to login view after successful signup
        viewManagerModel.setState("login");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SignupState signupState = signupViewModel.getState();

        // Set the appropriate error based on the error message
        if (errorMessage.contains("Username")) {
            signupState.setUsernameError(errorMessage);
        } else if (errorMessage.contains("Email")) {
            signupState.setEmailError(errorMessage);
        } else if (errorMessage.contains("Password") || errorMessage.contains("password")) {
            signupState.setPasswordError(errorMessage);
        } else if (errorMessage.contains("Full name")) {
            signupState.setFullNameError(errorMessage);
        } else {
            // Generic error - set it on username field
            signupState.setUsernameError(errorMessage);
        }

        signupViewModel.firePropertyChanged();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState("login");
        viewManagerModel.firePropertyChanged();
    }
}
