package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

public class LogoutPresenter implements LogoutOutputBoundary {
    public static final String LOGOUT_BUTTON = "Logout";

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public LogoutPresenter(LoginViewModel loginViewModel,
                           ViewManagerModel viewManagerModel) {
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData outputData) {
        // Set the last session username as a placeholder for the login form
        final LoginState loginState = loginViewModel.getState();
        loginState.setUsername(outputData.getUsername());

        // Switch back to login view after successful logout
        viewManagerModel.setState("login");
        viewManagerModel.firePropertyChanged();
    }
}
