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
        SwingUtilities.invokeLater(() -> {
            TranslationState translationState = translationViewModel.getState();
            translationState.setTranslatedText(outputData.getTranslatedText());
            translationState.setTargetLanguage(outputData.getTargetLanguage());
            translationState.setTranslationError(null); // Clear any previous error

            System.out.println("DEBUG: Presenter setting isTranslationSuccessful = TRUE");

            translationState.setTranslationSuccessful(true);

            this.translationViewModel.setState(translationState);
        });
    }

    /**
     * Handles a failed translation attempt, updating the ViewModel state with the error.
     * (Fixes: Implements the 'presentFailure' method from the Output Boundary)
     * @param errorMessage A descriptive error message.
     */
    @Override
    public void presentFailure(String errorMessage) {
        SwingUtilities.invokeLater(() -> {
            TranslationState translationState = translationViewModel.getState();

            translationState.setTranslationError("Error translating: " + errorMessage);
            translationState.setTranslatedText(null); // Clear previous successful translation

            translationState.setTranslationSuccessful(false);

            this.translationViewModel.setState(translationState);
        });
    }
}