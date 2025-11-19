package use_case.translate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation of the TranslationDataAccessInterface that
 * interacts with the Google Cloud Translation API via HTTP.
 *
 * This class encapsulates all external details (API key, URL, JSON parsing).
 */
public class TranslationDataAccess implements TranslationDataAccessInterface {

    // IMPORTANT: In a production environment, this key should be stored securely
    // (e.g., environment variables) and NEVER hardcoded.
    private static final String GOOGLE_API_KEY = "AIzaSyDBiXSSL6z57ri4AJXgKC_Wam_T_va0XB8";
    private static final String TRANSLATE_API_BASE_URL = "https://translation.googleapis.com/language/translate/v2";

    public static final List<String> SUPPORTED_LANGUAGES = List.of(
            "es", // Spanish
            "fr", // French
            "ja", // Japanese
            "de", // German
            "zh"  // Chinese (Simplified)
    );

    /**
     * Executes the API call to get the translation.
     *
     * @param text The text content to translate.
     * @param targetLangCode The target language code (e.g., "es" for Spanish).
     * @return The translated text, or an error message if translation fails.
     * @throws Exception if any network or API error occurs.
     */
    @Override
    public String translate(String text, String targetLangCode) throws Exception {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot translate empty text.");
        }
        if (targetLangCode == null || !SUPPORTED_LANGUAGES.contains(targetLangCode)) {
            throw new IllegalArgumentException("Invalid or unsupported target language code: " + targetLangCode);
        }

        // URL Encode the text content
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());

        // Build the full URL using GET query parameters
        String fullApiUrl = String.format(
                "%s?target=%s&key=%s&q=%s",
                TRANSLATE_API_BASE_URL,
                targetLangCode,
                GOOGLE_API_KEY,
                encodedText
        );

        URL url = new URL(fullApiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

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
            throw new Exception(String.format("Translation API Error (%d): %s", responseCode, apiResponse));
        }

        // Parse and return the translated text
        return parseTranslatedText(apiResponse);
    }

    /**
     * Parses the JSON response to extract the translated text.
     * Response structure: {"data": {"translations": [{"translatedText": "..."}]}}
     */
    private String parseTranslatedText(String apiResponse) throws Exception {
        // SimpleJsonParser (moved from TranslatorUI) is used here to avoid external libraries
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

                        // Unescape common JSON characters
                        return translatedText.replace("\\\"", "\"").replace("\\n", "\n").replace("\\/", "/");
                    }
                }
            }

            throw new Exception("JSON response was successful but could not find the translation text structure.");

        } catch (Exception jsonEx) {
            throw new Exception("Failed to parse JSON response. Raw Response: " + apiResponse +
                    "\nParsing Error: " + jsonEx.getMessage(), jsonEx);
        }
    }

    // --- SimpleJsonParser (Moved from original TranslatorUI to encapsulate API parsing) ---

    private static class SimpleJsonParser {
        public static Map<String, Object> parse(String json) throws Exception {
            String trimmed = json.trim();
            if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
                throw new Exception("Invalid JSON object format.");
            }
            String inner = trimmed.substring(1, trimmed.length() - 1).trim();
            return parseObject(inner);
        }

        @SuppressWarnings("unchecked")
        private static Map<String, Object> parseObject(String content) throws Exception {
            Map<String, Object> map = new HashMap<>();
            int i = 0;
            while (i < content.length()) {
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;
                if (content.charAt(i) != '"') throw new Exception("Expected start of key quote at: " + i);
                int keyStart = ++i;
                int keyEnd = content.indexOf('"', keyStart);
                if (keyEnd == -1) throw new Exception("Missing closing quote for key at: " + keyStart);
                String key = content.substring(keyStart, keyEnd);
                i = keyEnd + 1;

                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;
                if (content.charAt(i) != ':') throw new Exception("Expected colon after key at: " + i);
                i++;

                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;

                Object value;
                char startChar = content.charAt(i);

                if (startChar == '"') {
                    int valStart = ++i;
                    StringBuilder val = new StringBuilder();
                    while(i < content.length()) {
                        char c = content.charAt(i);
                        // Simple unescaped quote check (note: this is a very basic parser)
                        if (c == '"' && content.charAt(i-1) != '\\') {
                            break;
                        }
                        val.append(c);
                        i++;
                    }
                    if (i == content.length()) throw new Exception("Unclosed string value at: " + valStart);
                    value = val.toString().replace("\\\"", "\"").replace("\\n", "\n");
                    i++;
                } else if (startChar == '{') {
                    int objectEnd = findMatchingBrace(content, i);
                    value = parseObject(content.substring(i + 1, objectEnd));
                    i = objectEnd + 1;
                } else if (startChar == '[') {
                    int arrayEnd = findMatchingBracket(content, i);
                    value = parseArray(content.substring(i + 1, arrayEnd));
                    i = arrayEnd + 1;
                } else {
                    int valueEnd = content.indexOf(',', i);
                    if (valueEnd == -1) valueEnd = content.length();
                    value = content.substring(i, valueEnd).trim();
                    i = valueEnd;
                }

                map.put(key, value);

                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;

                if (i < content.length() && content.charAt(i) == ',') {
                    i++;
                } else if (i < content.length()) {
                    throw new Exception("Unexpected character after value at: " + i);
                }
            }
            return map;
        }

        private static List<Object> parseArray(String content) throws Exception {
            List<Object> list = new java.util.ArrayList<>();
            int i = 0;

            while (i < content.length()) {
                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;
                if (i >= content.length()) break;

                char startChar = content.charAt(i);
                Object value;

                if (startChar == '{') {
                    int objectEnd = findMatchingBrace(content, i);
                    value = parseObject(content.substring(i + 1, objectEnd));
                    i = objectEnd + 1;
                } else if (startChar == '"') {
                    int valStart = ++i;
                    StringBuilder val = new StringBuilder();
                    while(i < content.length()) {
                        char c = content.charAt(i);
                        if (c == '"' && content.charAt(i-1) != '\\') {
                            break;
                        }
                        val.append(c);
                        i++;
                    }
                    if (i == content.length()) throw new Exception("Unclosed string value at: " + valStart);
                    value = val.toString().replace("\\\"", "\"").replace("\\n", "\n");
                    i++;
                } else {
                    throw new Exception("Unsupported value type in array at: " + i);
                }

                list.add(value);

                while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;

                if (i < content.length() && content.charAt(i) == ',') {
                    i++;
                }
            }
            return list;
        }

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
}