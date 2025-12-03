package data_access;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Import the interface
import use_case.translate.TranslationDataAccessInterface;

/**
 * Concrete implementation of the TranslationDataAccessInterface.
 */
public class TranslationDataAccessObject implements TranslationDataAccessInterface {
    // Use the provided empty string
    private static final String API_KEY;
    private static final String TRANSLATE_API_BASE_URL = "https://translation.googleapis.com/language/translate/v2";
    private static final Set<String> SUPPORTED_LANGUAGES =
            Set.of("ar", "cn", "en", "es", "fr", "de", "hi", "it", "ja", "ko", "ru");

    // Key format: "postId_languageCode" (e.g., "123_fr")
    private final Map<String, String> translationCache = new HashMap<>();

    // Helper function for creating the unique cache key
    private String createCacheKey(long postId, String languageCode) {
        return postId + "_" + languageCode.toLowerCase(Locale.ROOT);
    }

    static {
        String key = null;
        try (InputStream input = new FileInputStream("secrets.properties")) {
            final Properties prop = new Properties();
            prop.load(input);
            key = prop.getProperty("GOOGLE_API_KEY");
            if (key == null || key.trim().isEmpty()) {
                System.err.println("FATAL ERROR: GOOGLE_API_KEY not found in secrets.properties.");
            }
        }
        catch (FileNotFoundException ex) {
            System.err.println("FATAL ERROR: secrets.properties file not found. "
                    + "Have you created it and added your API key?");
        }
        catch (Exception ex) {
            System.err.println("FATAL ERROR: Could not read secrets.properties: " + ex.getMessage());
        }
        API_KEY = key;
    }

    /**
     * The core function.
     *
     * @param text The text content to translate.
     * @param targetLang The target language code (e.g., "es" for Spanish).
     * @return The translated text, or an error message if translation fails.
     */
    public String getTranslation(String text, String targetLang) {
        HttpURLConnection connection = null;

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
            final String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

            final String fullApiUrl = String.format(
                    "%s?target=%s&key=%s&q=%s",
                    TRANSLATE_API_BASE_URL,
                    targetLang,
                    API_KEY,
                    encodedText
            );

            final URL url = new URL(fullApiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            // Get the response code (Blocking call)
            final int responseCode = connection.getResponseCode();

            // Read the response stream (either success or error stream)
            final BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            }
            else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            final StringBuilder response = new StringBuilder();
            String responseLine;
            // Read lines and join them, removing newline characters
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();

            final String apiResponse = response.toString();

            System.out.println("DEBUG: DAO Response Code: " + responseCode);

            if (responseCode != 200) {
                return String.format("ERROR: Translation API Error (%d): %s", responseCode, apiResponse);
            }

            // Parse the JSON response using the custom utility
            try {
                final Map<String, Object> root = SimpleJsonParser.parse(apiResponse);

                // Navigate the structure: root -> data -> translations (List)
                if (root.containsKey("data")) {
                    @SuppressWarnings("unchecked")
                    final Map<String, Object> data = (Map<String, Object>) root.get("data");

                    if (data.containsKey("translations")) {
                        @SuppressWarnings("unchecked")
                        final List<Object> translations = (List<Object>) data.get("translations");

                        if (!translations.isEmpty()) {
                            @SuppressWarnings("unchecked")
                            final Map<String, Object> firstTranslation = (Map<String, Object>) translations.get(0);

                            // Extract the text
                            final String translatedText = (String) firstTranslation.get("translatedText");

                            System.out.println("DEBUG: DAO Parsed Text: [" + translatedText + "]");
                            // Unescape common JSON characters that might be returned in the text value
                            return translatedText.replace("\\\"", "\"").replace("\\n",
                                    "\n").replace("\\/", "/");
                        }
                    }
                }

                return "ERROR: Translation Failed: "
                        + "JSON response was successful but could not find the translation text.";

            }
            catch (Exception jsonEx) {
                return "ERROR: Translation Failed: Failed to parse JSON response with utility. Raw Response: "
                        + apiResponse + "\nParsing Error: " + jsonEx.getMessage();
            }

        }
        catch (Exception ex) {
            // FIX: Better error logging
            System.err.println("FATAL I/O/TIMEOUT ERROR: " + ex.getMessage());
            return "ERROR: Translation Failed (Network/IO): " + ex.getMessage();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Saves a newly generated translation into the in-memory cache.
     * @param postId             The ID of the post.
     * @param targetLanguageCode The language code of the translation.
     * @param translatedText     The translated content to save.
     */
    @Override
    public void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText) {
        final String key = createCacheKey(postId, targetLanguageCode);
        translationCache.put(key, translatedText);
    }

    /**
     * A JSON parser to avoid external dependencies.
     * It parses the JSON string into standard Java Map and List objects.
     */
    private static final class SimpleJsonParser {
        public static Map<String, Object> parse(String json) throws Exception {
            // Trim and check for object start/end
            final String trimmed = json.trim();
            if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
                throw new Exception("Invalid JSON object format.");
            }
            // Remove outer braces and split into key-value pairs
            final String inner = trimmed.substring(1, trimmed.length() - 1).trim();

            // Use a simple state machine/tokenizing approach for robustness
            return parseObject(inner);
        }

        @SuppressWarnings("unchecked")
        private static Map<String, Object> parseObject(String content) throws Exception {
            final Map<String, Object> map = new HashMap<>();

            int i = 0;
            while (i < content.length()) {
                // Skip non-significant whitespace
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                    i++;
                }

                // Parse Key
                if (content.charAt(i) != '"') {
                    throw new Exception("Expected start of key quote at: " + i);
                }
                final int keyStart = ++i;
                final int keyEnd = content.indexOf('"', keyStart);
                if (keyEnd == -1) {
                    throw new Exception("Missing closing quote for key at: " + keyStart);
                }
                final String key = content.substring(keyStart, keyEnd);
                i = keyEnd + 1;

                // Skip whitespace and find colon
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                    i++;
                }
                if (content.charAt(i) != ':') {
                    throw new Exception("Expected colon after key at: " + i);
                }
                // Skip colon
                i++;

                // Skip whitespace before value
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                    i++;
                }

                // Parse Value
                final Object value;
                final char startChar = content.charAt(i);

                if (startChar == '"') {
                    // String value
                    final int valStart = ++i;
                    final StringBuilder val = new StringBuilder();
                    while (i < content.length()) {
                        final char c = content.charAt(i);
                        if (c == '"' && content.charAt(i - 1) != '\\') {
                            // Simple unescaped quote check
                            break;
                        }
                        val.append(c);
                        i++;
                    }
                    if (i == content.length()) {
                        throw new Exception("Unclosed string value at: " + valStart);
                    }
                    // Unescape
                    value = val.toString().replace("\\\"", "\"").replace("\\n", "\n");
                    // Skip closing quote
                    i++;
                }
                else if (startChar == '{') {
                    // Nested object
                    final int objectEnd = findMatchingBrace(content, i);
                    value = parseObject(content.substring(i + 1, objectEnd));
                    i = objectEnd + 1;
                }
                else if (startChar == '[') {
                    // Array (List)
                    final int arrayEnd = findMatchingBracket(content, i);
                    value = parseArray(content.substring(i + 1, arrayEnd));
                    i = arrayEnd + 1;
                }
                else {
                    // Simple value (number, boolean, null)
                    int valueEnd = content.indexOf(',', i);
                    if (valueEnd == -1) {
                        valueEnd = content.length();
                    }
                    final String simpleValue = content.substring(i, valueEnd).trim();
                    // Return as String for simplicity
                    value = simpleValue;
                    i = valueEnd;
                }

                map.put(key, value);

                // Skip whitespace and look for comma or end
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                    i++;
                }

                if (i < content.length() && content.charAt(i) == ',') {
                    // Skip comma and continue loop
                    i++;
                }
                else if (i < content.length()) {
                    // Unexpected characters remaining
                    throw new Exception("Unexpected character after value at: " + i);
                }
            }
            return map;
        }

        private static List<Object> parseArray(String content) throws Exception {
            final List<Object> list = new java.util.ArrayList<>();
            int i = 0;

            while (i < content.length()) {
                // Skip whitespace
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                    i++;
                }
                // End of array content
                if (i >= content.length()) {
                    break;
                }

                // Determine value type
                final char startChar = content.charAt(i);
                final Object value;

                if (startChar == '{') {
                    // Nested object
                    final int objectEnd = findMatchingBrace(content, i);
                    value = parseObject(content.substring(i + 1, objectEnd));
                    i = objectEnd + 1;
                }
                else {
                    // Handle strings/numbers for safety.
                    throw new Exception("Unsupported value type in array at: " + i);
                }

                list.add(value);

                // Skip whitespace and look for comma or end
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                    i++;
                }

                if (i < content.length() && content.charAt(i) == ',') {
                    // Skip comma and continue loop
                    i++;
                }
            }
            return list;
        }

        // Helper to find the matching closing brace for nested structures
        private static int findMatchingBrace(String json, int start) throws Exception {
            int count = 0;
            boolean inString = false;
            for (int i = start; i < json.length(); i++) {
                final char c = json.charAt(i);
                if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                    inString = !inString;
                }
                if (!inString) {
                    if (c == '{') {
                        count++;
                    }
                    else if (c == '}') {
                        count--;
                    }
                }
                if (count == 0 && i > start) {
                    return i;
                }
            }
            throw new Exception("Unmatched brace starting at: " + start);
        }

        // Helper to find the matching closing bracket for arrays
        private static int findMatchingBracket(String json, int start) throws Exception {
            int count = 0;
            boolean inString = false;
            for (int i = start; i < json.length(); i++) {
                final char c = json.charAt(i);
                if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                    inString = !inString;
                }
                if (!inString) {
                    if (c == '[') {
                        count++;
                    }
                    else if (c == ']') {
                        count--;
                    }
                }
                if (count == 0 && i > start) {
                    return i;
                }
            }
            throw new Exception("Unmatched bracket starting at: " + start);
        }
    }
}
