package use_case.translate;

/**
 * Data structure holding the result of the Translation use case.
 * Used by the Interactor to pass data to the Presenter.
 */
public class TranslationOutputData {
    private final String translatedText;
    private final String targetLanguage;
    private final String sourceLanguage;

    public TranslationOutputData(String translatedText, String targetLanguage, String sourceLanguage) {
        this.translatedText = translatedText;
        this.targetLanguage = targetLanguage;
        this.sourceLanguage = sourceLanguage;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }
}