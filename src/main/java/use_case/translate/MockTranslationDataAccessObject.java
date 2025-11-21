package use_case.translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of the TranslationDataAccessInterface for testing purposes.
 * It simulates language detection, translation, and now includes external caching logic.
 */
public class MockTranslationDataAccessObject implements TranslationDataAccessInterface {

    // Simulates the external cache storage:
    // Key: postId | targetLanguageCode (e.g., "1234|fr")
    // Value: Translated content
    private final Map<String, String> translationCache = new HashMap<>();

    // --- NEW METHODS FOR CACHING ---

    /**
     * Attempts to retrieve a cached translation for a given post ID and target language.
     */
    @Override
    public String getTranslatedContent(long postId, String targetLanguageCode) {
        // Construct the cache key
        String key = postId + "|" + targetLanguageCode;
        // Return the cached value or null
        return translationCache.get(key);
    }

    /**
     * Saves a newly generated translation into the mock cache map.
     */
    @Override
    public void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText) {
        // Construct and save using the cache key
        String key = postId + "|" + targetLanguageCode;
        translationCache.put(key, translatedText);
        System.out.println("MOCK: Translation for Post " + postId + " to " + targetLanguageCode + " saved to cache.");
    }

    // --- IMPLEMENTED METHODS (EXISTING OR NEW) ---

    /**
     * Simulates language detection. For testing, we can define a simple rule.
     */
    @Override
    public String detectLanguage(String sourceText) {
        // Simple mock logic: assume posts starting with "Hola" are Spanish, others English
        if (sourceText.toLowerCase().startsWith("hola")) {
            return "es"; // Spanish
        } else if (sourceText.toLowerCase().startsWith("bonjour")) {
            return "fr"; // French
        }
        return "en"; // English (default)
    }

    /**
     * Simulates the external translation service call.
     */
    @Override
    public String translate(String sourceText, String sourceLanguageCode, String targetLanguageCode) {
        if ("en".equalsIgnoreCase(targetLanguageCode)) {
            return sourceText; // No translation if target is English
        }
        // Mock translation logic based on source language
        if ("en".equalsIgnoreCase(sourceLanguageCode) && "es".equalsIgnoreCase(targetLanguageCode)) {
            return "MOCK_TRANSLATION: Spanish version of '" + sourceText.substring(0, Math.min(20, sourceText.length())) + "...'";
        } else if ("en".equalsIgnoreCase(sourceLanguageCode) && "fr".equalsIgnoreCase(targetLanguageCode)) {
            return "MOCK_TRANSLATION: French version of '" + sourceText.substring(0, Math.min(20, sourceText.length())) + "...'";
        }
        return "ERROR: Unsupported mock translation pair.";
    }

    /**
     * Checks if the target language is supported (mock implementation).
     */
    @Override
    public boolean isLanguageSupported(String languageCode) {
        // Mock: Only support English, Spanish, and French for simplicity
        return "en".equalsIgnoreCase(languageCode) ||
                "es".equalsIgnoreCase(languageCode) ||
                "fr".equalsIgnoreCase(languageCode);
    }
}