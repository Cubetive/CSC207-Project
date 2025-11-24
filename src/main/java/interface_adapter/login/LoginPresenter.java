package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(LoginViewModel loginViewModel,
                         ViewManagerModel viewManagerModel) {
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, clear the login form
        final LoginState loginState = loginViewModel.getState();
        loginState.setUsername("");
        loginState.setPassword("");
        loginState.setUsernameError(null);
        loginState.setPasswordError(null);

        loginViewModel.setState(loginState);
        loginViewModel.firePropertyChange();

        // Switch to browse posts view after successful login
        viewManagerModel.setState("browse posts");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final LoginState loginState = loginViewModel.getState();

        // Set the appropriate error based on the error message
        if (errorMessage.contains("password") || errorMessage.contains("Password")) {
            loginState.setPasswordError(errorMessage);
        } else {
            // Default to username error
            loginState.setUsernameError(errorMessage);
        }

        loginViewModel.firePropertyChange();
    }

    @Override
    public void switchToSignupView() {
        viewManagerModel.setState("sign up");
        viewManagerModel.firePropertyChanged();
    }
}
