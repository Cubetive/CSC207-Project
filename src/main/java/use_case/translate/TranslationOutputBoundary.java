package use_case.translate;

/**
 * The Output Boundary interface for the Translation use case.
 * This is implemented by the Presenter to ensure the Interactor only knows
 * what methods to call, not how the UI will display the results.
 */
public interface TranslationOutputBoundary {

    /**
     * Called when the translation is successful.
     * @param outputData The translated text data.
     */
    void presentSuccess(TranslationOutputData outputData);

    /**
     * Called when the translation or input validation fails.
     * @param errorMessage A descriptive error message.
     */
    void presentFailure(String errorMessage);
}