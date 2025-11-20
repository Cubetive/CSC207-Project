package interface_adapter.translate;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The ViewModel for the Translation Use Case.
 *
 * This holds the observable state (TranslationState) that the View listens to.
 * When the state changes (i.e., when the Presenter updates it), the View automatically
 * re-renders to display the new information (e.g., the translated text).
 */
public class TranslationViewModel extends ViewModel {

    // Define the name of the state property to be observed by the View
    public static final String STATE_PROPERTY_NAME = "state";

    // The current state held by the ViewModel
    private TranslationState state = new TranslationState();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Constructs the ViewModel, giving it a unique view name.
     */
    public TranslationViewModel() {
        super("translate");
    }

    /**
     * Notifies listeners (the View) that the state has changed.
     */
    public void firePropertyChanged() {
        support.firePropertyChange(STATE_PROPERTY_NAME, null, this.state);
    }

    /**
     * Adds a listener to observe state changes.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    // Getter and Setter for the current state
    public TranslationState getState() {
        return state;
    }

    public void setState(TranslationState state) {
        this.state = state;
    }
}