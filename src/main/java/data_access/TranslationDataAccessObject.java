package data_access;

import use_case.translate.TranslationDataAccessInterface; // Import the interface

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Concrete implementation of the TranslationDataAccessInterface.
 * It handles both external API calls (Gemini for detection/translation)
 * and simulated internal caching/persistence for performance.
 */
public class TranslationDataAccessObject implements TranslationDataAccessInterface {

    // --- Simulated Cache (Replaces a real database/Firestore collection) ---
    // Key format: "postId_languageCode" (e.g., "123_fr")
    private final Map<String, String> translationCache = new HashMap<>();

    // --- Gemini API Configuration ---
    // NOTE: In a real environment, load this from environment variables.
    private static final String apiKeyName = "GOOGLE_API_KEY";
    private static final String API_KEY = System.getenv(apiKeyName); // Use the provided empty string
    private static final String GOOGLE_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-09-2025:generateContent?key=" + API_KEY;
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    // Simple set of supported language codes for demonstration purposes
    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("en", "es", "fr", "de", "ja", "ko");

    // Helper function for creating the unique cache key
    private String createCacheKey(long postId, String languageCode) {
        return postId + "_" + languageCode.toLowerCase(Locale.ROOT);
    }

    /**
     * 1. Retrieves a cached translation. Implements the method required by the interface.
     */
    @Override
    public String getTranslatedContent(long postId, String targetLanguageCode) {
        String key = createCacheKey(postId, targetLanguageCode);
        return translationCache.get(key); // Returns null if not found
    }

    /**
     * 2. Saves a newly generated translation into the in-memory cache. Implements the method required by the interface.
     */
    @Override
    public void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText) {
        String key = createCacheKey(postId, targetLanguageCode);
        translationCache.put(key, translatedText);
        System.out.println("DEBUG: Translation for Post ID " + postId + " saved to cache.");
    }

    /**
     * 3. Detects the source language of the provided text using the Gemini API. Implements the method required by the interface.
     */
    @Override
    public String detectLanguage(String sourceText) {
        if (sourceText == null || sourceText.trim().isEmpty()) {
            return "unknown";
        }

        // System instruction guides the model to only output the language code.
        String systemPrompt = "You are a language detection service. Analyze the provided text and respond ONLY with the ISO 639-1 two-letter language code (e.g., 'en', 'fr', 'ja'). Do not include any other text, explanation, or punctuation.";
        String userQuery = "Detect the language of the following text: \"" + sourceText + "\"";

        try {
            String responseText = callApi(userQuery, systemPrompt, false);
            // Clean up the response (trim whitespace/quotes) before returning
            return responseText.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
        } catch (IOException | InterruptedException e) {
            System.err.println("ERROR: Failed to detect language using Gemini API: " + e.getMessage());
            return "unknown"; // Default fallback
        }
    }

    /**
     * 4. Calls the external translation service (Gemini) to translate the text. Implements the method required by the interface.
     */
    @Override
    public String translate(String sourceText, String sourceLanguageCode, String targetLanguageCode) {
        if (sourceText == null || sourceText.trim().isEmpty() || !isLanguageSupported(targetLanguageCode)) {
            return sourceText;
        }

        String systemPrompt = "You are a highly accurate, professional translator. Translate the user's text from " + sourceLanguageCode.toUpperCase(Locale.ROOT) + " to " + targetLanguageCode.toUpperCase(Locale.ROOT) + ". Respond ONLY with the direct translation text. Do not add any conversational text, explanations, or formatting.";
        String userQuery = "Please translate this text: \"" + sourceText + "\"";

        try {
            return callApi(userQuery, systemPrompt, false).trim();
        } catch (IOException | InterruptedException e) {
            System.err.println("ERROR: Failed to translate content using Gemini API: " + e.getMessage());
            return "Translation failed: Service unavailable.";
        }
    }

    /**
     * 5. Checks if the target language is supported. Implements the method required by the interface.
     */
    @Override
    public boolean isLanguageSupported(String languageCode) {
        return SUPPORTED_LANGUAGES.contains(languageCode.toLowerCase(Locale.ROOT));
    }


    /**
     * Private helper method to handle the actual HTTP request to the Gemini API.
     * Implements exponential backoff for robustness.
     */
    private String callApi(String userQuery, String systemPrompt, boolean useSearchGrounding)
            throws IOException, InterruptedException {
        String payloadTemplate = "{"
                + "\"contents\": [{ \"parts\": [{ \"text\": \"%s\" }] }],"
                + "\"systemInstruction\": { \"parts\": [{ \"text\": \"%s\" }] } %s"
                + "}";

        String toolsBlock = useSearchGrounding ? ", \"tools\": [{ \"google_search\": {} }]" : "";
        String escapedUserQuery = userQuery.replace("\"", "\\\"");
        String escapedSystemPrompt = systemPrompt.replace("\"", "\\\"");

        String jsonPayload = String.format(payloadTemplate, escapedUserQuery, escapedSystemPrompt, toolsBlock);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GOOGLE_API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        int maxRetries = 3;
        long delay = 1000; // 1 second

        for (int i = 0; i < maxRetries; i++) {
            try {
                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    // Simple JSON parsing to extract the text content
                    // Finds the first "text" field in the response.
                    String body = response.body();
                    int textStartIndex = body.indexOf("\"text\"");
                    if (textStartIndex != -1) {
                        int valueStartIndex = body.indexOf(':', textStartIndex) + 1;
                        int valueEndIndex = body.indexOf('"', valueStartIndex);
                        // Find the second quote after the colon for the start of the actual text
                        if (valueEndIndex != -1) {
                            int actualTextStart = body.indexOf('"', valueEndIndex + 1);
                            if (actualTextStart != -1) {
                                int actualTextEnd = body.indexOf('"', actualTextStart + 1);
                                if (actualTextEnd != -1) {
                                    return body.substring(actualTextStart + 1, actualTextEnd);
                                }
                            }
                        }
                    }
                    return "ERROR: Could not parse API response.";
                } else if (response.statusCode() == 429 && i < maxRetries - 1) {
                    // 429 Too Many Requests -> Wait and retry
                    Thread.sleep(delay);
                    delay *= 2; // Exponential backoff
                } else {
                    throw new IOException("API request failed with status code: " + response.statusCode());
                }
            } catch (IOException e) {
                if (i < maxRetries - 1) {
                    Thread.sleep(delay);
                    delay *= 2;
                } else {
                    throw e; // Re-throw if last attempt failed
                }
            }
        }
        throw new IOException("API request failed after " + maxRetries + " retries.");
    }
}