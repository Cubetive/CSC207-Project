package interface_adapter.translate;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Translation Use Case.
 * This holds the observable state (TranslationState) that the View listens to.
 * When the state changes (i.e., when the Presenter updates it), the View automatically
 * re-renders to display the new information (e.g., the translated text).
 */
public class TranslationViewModel extends ViewModel {

    // Define the name of the state property to be observed by the View
    public static final String STATE_PROPERTY_NAME = "translationState";

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
     *
     * @param listener the property change listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Gets the current translation state.
     *
     * @return the current state
     */
    public TranslationState getState() {
        return state;
    }

    /**
     * Sets the current state and fires a property change event.
     *
     * @param newState the new state to set
     */
    public void setState(TranslationState newState) {
        this.state = newState;
        support.firePropertyChange(STATE_PROPERTY_NAME, null, this.state);
    }
}
