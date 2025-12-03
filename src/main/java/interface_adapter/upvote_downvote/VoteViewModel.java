package interface_adapter.upvote_downvote;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import interface_adapter.ViewModel;

/**
 * ViewModel for the Vote Use Case.
 * Holds the current VoteState and notifies the View when the state changes.
 */
public class VoteViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Vote View";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private VoteState state = new VoteState();

    public VoteViewModel() {
        super("vote");
    }

    public void setState(VoteState state) {
        this.state = state;
    }

    /**
     * This is called by the Presenter to signal a change.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    /**
     * Adds Property Change Listener.
     * @param listener the listener for property change
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public VoteState getState() {
        return state;
    }
}
