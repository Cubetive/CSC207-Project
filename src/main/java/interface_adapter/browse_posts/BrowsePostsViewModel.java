package interface_adapter.browse_posts;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The View Model for the Browse Posts View.
 */
public class BrowsePostsViewModel {

    public static final String TITLE_LABEL = "Browse Posts";
    public static final String REFRESH_BUTTON_LABEL = "Refresh";

    private BrowsePostsState state = new BrowsePostsState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public BrowsePostsViewModel() {
    }

    public void setState(BrowsePostsState state) {
        this.state = state;
    }

    public BrowsePostsState getState() {
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
