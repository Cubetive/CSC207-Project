package interface_adapter.translate;

import use_case.translate.TranslationOutputBoundary;
import use_case.translate.TranslationOutputData;
import interface_adapter.ViewManagerModel;
import javax.swing.SwingUtilities; // FIX: Import SwingUtilities

/**
 * Implements the TranslationOutputBoundary. It receives the Use Case result,
 * transforms the raw output data into a displayable TranslationState, and
 * updates the TranslationViewModel to notify the View.
 */
public class TranslationPresenter implements TranslationOutputBoundary {

    private final TranslationViewModel translationViewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs a TranslationPresenter.
     * @param translationViewModel The ViewModel to update.
     * @param viewManagerModel The model to switch views (usually not needed for inline translation).
     */
    public TranslationPresenter(TranslationViewModel translationViewModel,
                                ViewManagerModel viewManagerModel) {
        this.translationViewModel = translationViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Handles a successful translation result, updating the ViewModel state.
     * @param outputData The data object containing the translated text and metadata.
     */
    @Override
    public void presentSuccess(TranslationOutputData outputData) {
        // FIX: MUST execute ViewModel update on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // 1. Get the current state
            TranslationState translationState = translationViewModel.getState();

            // 2. Update the state with new data
            translationState.setTranslatedText(outputData.getTranslatedText());
            // translationState.setSourceLanguage(outputData.getSourceLanguage()); // FIX: Commented out as source language isn't in OutputData
            translationState.setTargetLanguage(outputData.getTargetLanguage());
            translationState.setTranslationError(null); // Clear any previous error

            // 3. Fire the property change event to notify the View
            this.translationViewModel.setState(translationState);
            this.translationViewModel.firePropertyChanged();
        });
    }

    /**
     * Handles a failed translation attempt, updating the ViewModel state with the error.
     * (Fixes: Implements the 'presentFailure' method from the Output Boundary)
     * @param errorMessage A descriptive error message.
     */
    @Override
    public void presentFailure(String errorMessage) {
        // FIX: MUST execute ViewModel update on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // 1. Get the current state
            TranslationState translationState = translationViewModel.getState();

            // 2. Update the state with the error
            translationState.setTranslationError("Error translating: " + errorMessage);
            translationState.setTranslatedText(null); // Clear previous successful translation

            // 3. Fire the property change event to notify the View
            this.translationViewModel.setState(translationState);
            this.translationViewModel.firePropertyChanged();
        });
    }
}