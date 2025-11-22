package use_case.translate;

/**
 * Data structure holding the result of the Translation use case.
 * Used by the Interactor to pass data to the Presenter.
 */
public class TranslationOutputData {
    private final String translatedText;
    private final String targetLanguage;
    private final long postId;
    private final boolean isFromCache; // Flag to indicate if the translation came from the cache

    /**
     * Constructs the output data.
     * @param translatedText The final translated text.
     * @param targetLanguage The target language code (e.g., "fr", "es").
     * @param postId The ID of the original post.
     * @param isFromCache True if the result was loaded from the cache, false otherwise.
     */
    public TranslationOutputData(String translatedText, String targetLanguage, long postId, boolean isFromCache) {
        this.translatedText = translatedText;
        this.targetLanguage = targetLanguage;
        this.postId = postId;
        this.isFromCache = isFromCache;
    }

    // Getters
    public String getTranslatedText() {
        return translatedText;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public long getPostId() {
        return postId;
    }

    public boolean isFromCache() {
        return isFromCache;
    }
}