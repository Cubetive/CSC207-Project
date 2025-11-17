package interface_adapter.signup;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The View Model for the Signup View.
 */
public class SignupViewModel {

    public static final String TITLE_LABEL = "Sign Up";
    public static final String FULLNAME_LABEL = "Full Name";
    public static final String USERNAME_LABEL = "Username";
    public static final String EMAIL_LABEL = "Email";
    public static final String PASSWORD_LABEL = "Password";
    public static final String REPEAT_PASSWORD_LABEL = "Repeat Password";
    public static final String SIGNUP_BUTTON_LABEL = "Sign Up";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";
    public static final String TO_LOGIN_BUTTON_LABEL = "Go to Login";

    private SignupState state = new SignupState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public SignupViewModel() {
    }

    public void setState(SignupState state) {
        this.state = state;
    }

    public SignupState getState() {
        return state;
    }

    /**
     * Fires a property change event to notify observers.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
