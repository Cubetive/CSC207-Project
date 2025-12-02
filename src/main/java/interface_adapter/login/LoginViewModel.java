package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {

    public static final String TITLE_LABEL = "Log In";
    public static final String USERNAME_LABEL = "Username";
    public static final String PASSWORD_LABEL = "Password";
    public static final String LOGIN_BUTTON_LABEL = "Log In";
    public static final String TO_SIGNUP_BUTTON_LABEL = "Go to Sign Up";

    /**
     * Constructs a LoginViewModel.
     */
    public LoginViewModel() {
        super("login");
        setState(new LoginState());
    }
}
