package interface_adapter.translate;

import javax.swing.SwingUtilities;

import interface_adapter.ViewManagerModel;
import use_case.translate.TranslationOutputBoundary;
import use_case.translate.TranslationOutputData;

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
     *
     * @param translationViewModel the ViewModel to update
     * @param viewManagerModel     the model to switch views
     */
    public TranslationPresenter(TranslationViewModel translationViewModel,
                                ViewManagerModel viewManagerModel) {
        this.translationViewModel = translationViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Handles a successful translation result, updating the ViewModel state.
     *
     * @param outputData the data object containing the translated text and metadata
     */
    @Override
    public void presentSuccess(TranslationOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            final TranslationState translationState = translationViewModel.getState();
            translationState.setTranslatedText(outputData.getTranslatedText());
            translationState.setTargetLanguage(outputData.getTargetLanguage());
            translationState.setTranslationError(null);

            translationState.setTranslationSuccessful(true);

            this.translationViewModel.setState(translationState);
        });
    }

    /**
     * Handles a failed translation attempt, updating the ViewModel state with the error.
     *
     * @param errorMessage a descriptive error message
     */
    @Override
    public void presentFailure(String errorMessage) {
        SwingUtilities.invokeLater(() -> {
            final TranslationState translationState = translationViewModel.getState();

            translationState.setTranslationError("Error translating: " + errorMessage);
            translationState.setTranslatedText(null);

            translationState.setTranslationSuccessful(false);

            this.translationViewModel.setState(translationState);
        });
    }
}
