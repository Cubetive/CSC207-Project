package use_case.translate;

/**
 * Data structure holding the result of the Translation use case.
 * Used by the Interactor to pass data to the Presenter.
 */
public class TranslationOutputData {
    private final String translatedText;

    public TranslationOutputData(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getTranslatedText() {
        return translatedText;
    }
}