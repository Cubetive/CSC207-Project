package interface_adapter.upvote_downvote;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for the Vote Use Case.
 * Holds the current VoteState and notifies the View when the state changes.
 */
public class VoteViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Vote View"; // Not strictly necessary for this invisible process

    private VoteState state = new VoteState();

    public VoteViewModel() {
        super("vote");
    }

    public void setState(VoteState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // This is called by the Presenter to signal a change
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public VoteState getState() {
        return state;
    }
}