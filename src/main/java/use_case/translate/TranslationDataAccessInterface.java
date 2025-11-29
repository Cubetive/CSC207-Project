package use_case.translate;

public interface TranslationDataAccessInterface {

    /**
     * Attempts to retrieve a cached translation for a given post ID and target language.
     * This replaces the need for post.getTranslation().
     *
     * @param text               The original text.
     * @param targetLanguageCode The language code for the desired translation.
     * @return The translated content if cached, otherwise null.
     */
    String getTranslation(String text, String targetLanguageCode);

    /**
     * Saves a newly generated translation into the persistent cache.
     * This replaces the need for post.addTranslation().
     *
     * @param postId             The ID of the post.
     * @param targetLanguageCode The language code of the translation.
     * @param translatedText     The translated content to save.
     */
    void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText);
}

