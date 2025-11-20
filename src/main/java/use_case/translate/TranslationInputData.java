package use_case.translate;

/**
 * Data structure holding the input necessary to run the Translation use case.
 * Used by the Controller to pass data to the Interactor.
 */
public class TranslationInputData {
    private final String sourceLanguage;
    private final String originalText;
    private final String targetLanguageCode;

    public TranslationInputData(String sourceText, String targetLanguageCode, String sourceLanguage) {
        this.originalText = sourceText;
        this.targetLanguageCode = targetLanguageCode;
        this.sourceLanguage = sourceLanguage;
    }

    public String getSourceText() {
        return originalText;
    }

    public String getTargetLanguage() {
        return targetLanguageCode;
    }
    public String getSourceLanguage() {
        return sourceLanguage;
    }
}