package data_access;

import use_case.translate.TranslationDataAccessInterface;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit; // Import for TimeUnit
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture; // Import for CompletableFuture

/**
 * Concrete implementation of the TranslationDataAccessInterface.
 * Fix: Uses the robust HttpClient with the working GET method and includes a critical timeout mechanism
 * to prevent indefinite thread blocking.
 */
public class TranslationDataAccessObject implements TranslationDataAccessInterface {

    // --- Simulated Cache ---
    private final Map<String, String> translationCache = new HashMap<>();

    // --- Google Translation API Configuration ---
    private static final String apiKeyName = "GOOGLE_API_KEY";
    private static final String API_KEY = System.getenv(apiKeyName);
    private static final String TRANSLATE_API_BASE_URL = "https://translation.googleapis.com/language/translate/v2";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("en", "es", "fr", "de", "ja", "ko");
    private static final long API_TIMEOUT_SECONDS = 15;

    private String createCacheKey(long postId, String languageCode) {
        return postId + "_" + languageCode.toLowerCase(Locale.ROOT);
    }

    /**
     * The core function, using the modern, non-blocking HttpClient with a guaranteed timeout.
     *
     * @param text The text content to translate.
     * @param targetLang The target language code (e.g., "es" for Spanish).
     * @return The translated text, or an error message prefixed with "ERROR:" if translation fails.
     */
    public String getTranslation(String text, String targetLang) {
        if (text == null || text.trim().isEmpty()) {
            return "ERROR: Cannot translate empty text.";
        }
        if (targetLang == null || !SUPPORTED_LANGUAGES.contains(targetLang)) {
            return "ERROR: Invalid or unsupported target language code provided: " + targetLang;
        }

        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            return "ERROR: Missing API Key. Please set the GOOGLE_API_KEY environment variable.";
        }

        try {
            // 1. URL Encode the text content
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());

            // 2. Build the full URL using GET query parameters (Proven working pattern)
            String fullApiUrl = String.format(
                    "%s?target=%s&key=%s&q=%s",
                    TRANSLATE_API_BASE_URL,
                    targetLang,
                    API_KEY,
                    encodedText
            );

            // 3. Build the HTTP Request (GET method)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullApiUrl))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            // 4. Send the request ASYNCHRONOUSLY and apply a timeout.
            CompletableFuture<HttpResponse<String>> responseFuture = HTTP_CLIENT
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> response = responseFuture
                    .completeOnTimeout(
                            null,
                            API_TIMEOUT_SECONDS,
                            TimeUnit.SECONDS
                    )
                    .join(); // Block the background thread for the result (or the timeout)

            if (response == null) {
                return "ERROR: Translation Request Timed Out after " + API_TIMEOUT_SECONDS + " seconds.";
            }

            String apiResponse = response.body();
            int responseCode = response.statusCode();

            System.out.println("DEBUG: API Response Code: " + responseCode);
            System.out.println("DEBUG: API Response Body (Partial): " + apiResponse.substring(0, Math.min(200, apiResponse.length())));


            if (responseCode != 200) {
                return String.format("ERROR: Translation API Error (%d): %s", responseCode, apiResponse);
            }

            // 5. Parse the JSON response
            try {
                Map<String, Object> root = SimpleJsonParser.parse(apiResponse);

                if (root.containsKey("data")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> data = (Map<String, Object>) root.get("data");

                    if (data.containsKey("translations")) {
                        @SuppressWarnings("unchecked")
                        List<Object> translations = (List<Object>) data.get("translations");

                        if (!translations.isEmpty()) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> firstTranslation = (Map<String, Object>) translations.get(0);

                            String translatedText = (String) firstTranslation.get("translatedText");

                            return translatedText.replace("\\\"", "\"").replace("\\n",
                                    "\n").replace("\\/", "/");
                        }
                    }
                }

                return "ERROR: Translation Failed: JSON response was successful but could not find the translation text.";

            } catch (Exception jsonEx) {
                return "ERROR: Translation Failed: Failed to parse JSON response. Parsing Error: " + jsonEx.getMessage();
            }

        } catch (Exception e) {
            System.err.println("FATAL NETWORK/IO ERROR: " + e.getMessage());
            return "ERROR: Translation Failed (Network/IO): " + e.getMessage();
        }
    }


    /**
     * A JSON parser to avoid external dependencies.
     */
    private static class SimpleJsonParser {
        public static Map<String, Object> parse(String json) throws Exception {
            // Trim and check for object start/end
            String trimmed = json.trim();
            if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
                throw new Exception("Invalid JSON object format.");
            }
            // Remove outer braces and split into key-value pairs
            String inner = trimmed.substring(1, trimmed.length() - 1).trim();

            // Use a simple state machine/tokenizing approach for robustness
            return parseObject(inner);
        }

        @SuppressWarnings("unchecked")
        private static Map<String, Object> parseObject(String content) throws Exception {
            Map<String, Object> map = new HashMap<>();

            int i = 0;
            while (i < content.length()) {
                // Skip non-significant whitespace
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;

                // Parse Key
                if (content.charAt(i) != '"') throw new Exception("Expected start of key quote at: " + i);
                int keyStart = ++i;
                int keyEnd = content.indexOf('"', keyStart);
                if (keyEnd == -1) throw new Exception("Missing closing quote for key at: " + keyStart);
                String key = content.substring(keyStart, keyEnd);
                i = keyEnd + 1;

                // Skip whitespace and find colon
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;
                if (content.charAt(i) != ':') throw new Exception("Expected colon after key at: " + i);
                i++; // Skip colon

                // Skip whitespace before value
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;

                // Parse Value
                Object value;
                char startChar = content.charAt(i);

                if (startChar == '"') {
                    // String value
                    int valStart = ++i;
                    StringBuilder val = new StringBuilder();
                    while(i < content.length()) {
                        char c = content.charAt(i);
                        if (c == '"' && content.charAt(i-1) != '\\') { // Simple unescaped quote check
                            break;
                        }
                        val.append(c);
                        i++;
                    }
                    if (i == content.length()) throw new Exception("Unclosed string value at: " + valStart);
                    value = val.toString().replace("\\\"", "\"").replace("\\n", "\n"); // Unescape
                    i++; // Skip closing quote
                } else if (startChar == '{') {
                    // Nested object
                    int objectEnd = findMatchingBrace(content, i);
                    value = parseObject(content.substring(i + 1, objectEnd));
                    i = objectEnd + 1;
                } else if (startChar == '[') {
                    // Array (List)
                    int arrayEnd = findMatchingBracket(content, i);
                    value = parseArray(content.substring(i + 1, arrayEnd));
                    i = arrayEnd + 1;
                } else {
                    // Simple value (number, boolean, null) - not strictly needed for this API response, but good practice
                    int valueEnd = content.indexOf(',', i);
                    if (valueEnd == -1) valueEnd = content.length();
                    String simpleValue = content.substring(i, valueEnd).trim();
                    value = simpleValue; // Return as String for simplicity
                    i = valueEnd;
                }

                map.put(key, value);

                // Skip whitespace and look for comma or end
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;

                if (i < content.length() && content.charAt(i) == ',') {
                    i++; // Skip comma and continue loop
                } else if (i < content.length()) {
                    // Unexpected characters remaining
                    throw new Exception("Unexpected character after value at: " + i);
                }
            }
            return map;
        }

        private static List<Object> parseArray(String content) throws Exception {
            List<Object> list = new java.util.ArrayList<>();
            int i = 0;

            while (i < content.length()) {
                // Skip whitespace
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;
                if (i >= content.length()) break; // End of array content

                // Determine value type
                char startChar = content.charAt(i);
                Object value;

                if (startChar == '{') {
                    // Nested object
                    int objectEnd = findMatchingBrace(content, i);
                    value = parseObject(content.substring(i + 1, objectEnd));
                    i = objectEnd + 1;
                } else {
                    // Handle strings/numbers for safety.
                    throw new Exception("Unsupported value type in array at: " + i);
                }

                list.add(value);

                // Skip whitespace and look for comma or end
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;

                if (i < content.length() && content.charAt(i) == ',') {
                    i++; // Skip comma and continue loop
                }
            }
            return list;
        }

        // Helper to find the matching closing brace for nested structures
        private static int findMatchingBrace(String json, int start) throws Exception {
            int count = 0;
            boolean inString = false;
            for (int i = start; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                    inString = !inString;
                }
                if (!inString) {
                    if (c == '{') count++;
                    else if (c == '}') count--;
                }
                if (count == 0 && i > start) return i;
            }
            throw new Exception("Unmatched brace starting at: " + start);
        }

        // Helper to find the matching closing bracket for arrays
        private static int findMatchingBracket(String json, int start) throws Exception {
            int count = 0;
            boolean inString = false;
            for (int i = start; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                    inString = !inString;
                }
                if (!inString) {
                    if (c == '[') count++;
                    else if (c == ']') count--;
                }
                if (count == 0 && i > start) return i;
            }
            throw new Exception("Unmatched bracket starting at: " + start);
        }
    }

    /**
     * Saves a newly generated translation into the in-memory cache.
     */
    @Override
    public void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText) {
        String key = createCacheKey(postId, targetLanguageCode);
        translationCache.put(key, translatedText);
        System.out.println("DEBUG: Translation for Post ID " + postId + " saved to cache.");
    }
}