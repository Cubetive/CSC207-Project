package interface_adapter.signup;

import interface_adapter.ViewModel;

/**
 * The View Model for the Signup View.
 */
public class SignupViewModel extends ViewModel<SignupState> {

    public static final String TITLE_LABEL = "Sign Up";
    public static final String FULLNAME_LABEL = "Full Name";
    public static final String USERNAME_LABEL = "Username";
    public static final String EMAIL_LABEL = "Email";
    public static final String PASSWORD_LABEL = "Password";
    public static final String REPEAT_PASSWORD_LABEL = "Repeat Password";
    public static final String SIGNUP_BUTTON_LABEL = "Sign Up";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";
    public static final String TO_LOGIN_BUTTON_LABEL = "Go to Login";

    public SignupViewModel() {
        super("sign up");
        setState(new SignupState());
    }
}
