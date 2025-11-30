package interface_adapter.reference_post;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The View Model for the Reference Post View.
 */
public class ReferencePostViewModel {

    public static final String TITLE_LABEL = "Reference a Post";
    public static final String SEARCH_LABEL = "Search Keyword";
    public static final String SEARCH_BUTTON_LABEL = "Search";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";
    public static final String REFERENCE_BUTTON_LABEL = "Reference";
    public static final String NO_RESULTS_MESSAGE = "No posts found.";

    private ReferencePostState state = new ReferencePostState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ReferencePostViewModel() {
    }

    public void setState(ReferencePostState state) {
        this.state = state;
    }

    public ReferencePostState getState() {
        return state;
    }

    /**
     * Fires a property change event to notify observers.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    /**
     * Fires a property change event with specific property name.
     * @param propertyName the name of the property that changed
     */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
