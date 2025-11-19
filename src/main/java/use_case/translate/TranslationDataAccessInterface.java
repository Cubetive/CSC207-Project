package use_case.translate;

/**
 * The interface for the Data Access component, providing the contract
 * for any service that can perform text translation.
 *
 * This interface isolates the core business logic (TranslationInteractor)
 * from the external API details (GoogleTranslateDataAccess).
 */
public interface TranslationDataAccessInterface {
    /**
     * Retrieves the translated text from the external service.
     *
     * @param text The source text content to translate.
     * @param targetLangCode The target language code (e.g., "es", "fr").
     * @return The translated text string.
     * @throws Exception if the network request or API call fails.
     */
    String translate(String text, String targetLangCode) throws Exception;
}