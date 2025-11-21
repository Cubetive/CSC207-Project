package use_case.translate;

public interface TranslationDataAccessInterface {

    /**
     * Attempts to retrieve a cached translation for a given post ID and target language.
     * This replaces the need for post.getTranslation().
     *
     * @param postId The ID of the post whose translation is being requested.
     * @param targetLanguageCode The language code for the desired translation.
     * @return The translated content if cached, otherwise null.
     */
    String getTranslatedContent(long postId, String targetLanguageCode);

    /**
     * Saves a newly generated translation into the persistent cache.
     * This replaces the need for post.addTranslation().
     *
     * @param postId The ID of the post.
     * @param targetLanguageCode The language code of the translation.
     * @param translatedText The translated content to save.
     */
    void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText);

    /**
     * Detects the source language of the provided text content.
     *
     * @param sourceText The text content of the post.
     * @return The two-letter source language code (e.g., "en", "es").
     */
    String detectLanguage(String sourceText);

    /**
     * Calls the external translation service to translate the text.
     */
    String translate(String sourceText, String sourceLanguageCode, String targetLanguageCode);

    /**
     * Checks if the target language is supported by the translation service.
     */
    boolean isLanguageSupported(String languageCode);
}