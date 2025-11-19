package use_case.translate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Concrete implementation of the TranslationDataAccessInterface that
 * interacts with the Google Cloud Translation API (V2) via HTTP GET.
 *
 * NOTE: This class is placed in the 'use_case.translate' package, which is
 * unconventional for a Data Access Object (DAO). A DAO should typically
 * reside in a 'data_access' package.
 */
public class TranslationDataAccess implements TranslationDataAccessInterface {

    // The environment variable name used to retrieve the API key
    private static final String apiKeyName = "GOOGLE_API_KEY";

    // Retrieve the API key from the OS environment variables
    private static final String apiKey = System.getenv(apiKeyName);

    private static final String TRANSLATE_API_BASE_URL = "https://translation.googleapis.com/language/translate/v2";

    public static final List<String> SUPPORTED_LANGUAGES = List.of(
            "es", // Spanish
            "fr", // French
            "ja", // Japanese
            "de", // German
            "zh"  // Chinese (Simplified)
    );

    /**
     * Checks if the given language code is supported by our application configuration.
     * This implementation has been corrected to return a boolean instead of throwing an exception.
     *
     * @param targetLangCode The target language code (e.g., "es").
     * @return true if the language is supported, false otherwise.
     */
    @Override
    public boolean isLanguageSupported(String targetLangCode) {
        if (targetLangCode == null) {
            return false;
        }
        // CORRECTED: Returns true or false, adhering to the interface contract.
        return SUPPORTED_LANGUAGES.contains(targetLangCode.toLowerCase());
    }

    /**
     * Executes the API call to get the translation using an HTTP GET request.
     *
     * @param text The text content to translate.
     * @param targetLangCode The target language code (e.g., "es" for Spanish).
     * @return The translated text, or an error message if translation fails.
     */
    @Override
    public String translate(String text, String targetLangCode) {
        if (text == null || text.trim().isEmpty()) {
            return "ERROR: Cannot translate empty text.";
        }

        // Use the non-throwing check here
        if (!isLanguageSupported(targetLangCode)) {
            return "ERROR: Invalid or unsupported target language code: " + targetLangCode;
        }

        // 1. Check for API key presence
        if (apiKey == null || apiKey.isEmpty()) {
            return "ERROR: Google API Key is not set in the environment variable " + apiKeyName;
        }

        try {
            // URL Encode the text content
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());

            // Build the full URL using GET query parameters (limited to 2K characters total URL length)
            String fullApiUrl = String.format(
                    "%s?target=%s&key=%s&q=%s",
                    TRANSLATE_API_BASE_URL,
                    targetLangCode,
                    apiKey,
                    encodedText
            );

            URL url = new URL(fullApiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000); // 5 seconds
            connection.setReadTimeout(5000);    // 5 seconds

            int responseCode = connection.getResponseCode();

            // Read the response stream (either success or error stream)
            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();

            String apiResponse = response.toString();

            if (responseCode != 200) {
                // Your fix using String concatenation is retained here:
                return "ERROR: Translation API Error (" + responseCode + "): " + apiResponse;
            }

            // OPTIMIZED: Use robust substring search to parse the fixed JSON structure
            return parseTranslatedText(apiResponse);

        } catch (Exception e) {
            System.err.println("Network or I/O Error during translation: " + e.getMessage());
            return "ERROR: Translation Failed (Network/IO): " + e.getMessage();
        }
    }
    public boolean isLanguageSupported(String targetLangCode) {
        if  (targetLangCode == null || !SUPPORTED_LANGUAGES.contains(targetLangCode)) {
            throw new IllegalArgumentException("Invalid target language code: " + targetLangCode);
        }
        else return true;
    }

    /**
     * Parses the JSON response to extract the translated text using substring search.
     * This is far simpler and less error-prone than a custom JSON parser for a fixed API response.
     * Response structure: {"data": {"translations": [{"translatedText": "..."}]}}
     */
    private String parseTranslatedText(String apiResponse) {
        String textMarker = "\"translatedText\":\"";
        int startIndex = apiResponse.indexOf(textMarker);

        if (startIndex == -1) {
            return "ERROR: Translation Failed: Could not find translated text in response. Raw Response: " + apiResponse;
        }

        startIndex += textMarker.length();
        // Look for the closing quote, skipping past the start index
        int endIndex = apiResponse.indexOf("\"", startIndex);

        if (endIndex == -1) {
            return "ERROR: Translation Failed: Could not find closing quote. Raw Response: " + apiResponse;
        }

        String translatedText = apiResponse.substring(startIndex, endIndex);

        // Unescape common JSON characters
        return translatedText.replace("\\\"", "\"").replace("\\n", "\n").replace("\\/", "/");
    }
}