package interface_adapter.edit_profile;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The View Model for the Edit Profile View.
 */
public class EditProfileViewModel {

    public static final String TITLE_LABEL = "Edit Profile";
    public static final String USERNAME_LABEL = "Username";
    public static final String FULLNAME_LABEL = "Full Name";
    public static final String BIO_LABEL = "Bio";
    public static final String PROFILE_PICTURE_LABEL = "Profile Picture URL";
    public static final String CURRENT_PASSWORD_LABEL = "Current Password";
    public static final String NEW_PASSWORD_LABEL = "New Password";
    public static final String REPEAT_NEW_PASSWORD_LABEL = "Repeat New Password";
    public static final String SAVE_BUTTON_LABEL = "Save Changes";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    private EditProfileState state = new EditProfileState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public EditProfileViewModel() {
    }

    public void setState(EditProfileState state) {
        this.state = state;
    }

    public EditProfileState getState() {
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
