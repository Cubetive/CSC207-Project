package use_case.translate;

/**
 * Data structure holding the input necessary to run the Translation use case.
 * Used by the Controller to pass data to the Interactor.
 */
public class TranslationInputData {
    private final String sourceText;
    private final String targetLanguageCode;

    public TranslationInputData(String sourceText, String targetLanguageCode) {
        this.sourceText = sourceText;
        this.targetLanguageCode = targetLanguageCode;
    }

    public String getSourceText() {
        return sourceText;
    }

    public String getTargetLanguageCode() {
        return targetLanguageCode;
    }
}