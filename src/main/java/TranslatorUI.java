import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;
import java.util.Map;

/**
 * A Java Swing application for demonstrating Google Cloud Text Translation.
 * This version requires the user to type in the text they want to translate.
 */
public class TranslatorUI extends JFrame {

    // Configuration for Google Cloud Translation API

    private static final String apiKeyName = "GOOGLE_API_KEY";
    private static final String apiKey = System.getenv(apiKeyName);
    private static final String TRANSLATE_API_BASE_URL = "https://translation.googleapis.com/language/translate/v2";

    public static final List<String> SUPPORTED_LANGUAGES = List.of(
            "es", // Spanish
            "fr", // French
            "ja", // Japanese
            "de", // German
            "zh"  // Chinese (Simplified)
    );

    // UI components
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JComboBox<String> languageChooser;
    private JButton translateButton;

    public TranslatorUI() {
        super("Translator UI (Demo)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        setupLayout();

        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void initComponents() {
        // Input Area
        inputArea = new JTextArea("Hello! Please enter the text you want to translate here.", 10, 30);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(BorderFactory.createTitledBorder("Input Text (Source language is auto-detected)"));

        // Output Area
        outputArea = new JTextArea("Translated text will appear here...", 10, 30);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createTitledBorder("Translated Text"));

        // Language Chooser
        List<String> langs = SUPPORTED_LANGUAGES;
        String[] langArray = langs.toArray(new String[0]);
        languageChooser = new JComboBox<>(langArray);
        languageChooser.setSelectedItem("es");

        // Button Setup
        translateButton = new JButton("Translate");
        translateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        translateButton.addActionListener(new TranslateAction());
    }

    private void setupLayout() {
        // Use BorderLayout for main structure
        setLayout(new BorderLayout(10, 10));

        // Center Panel for Input/Output (split panel)
        JPanel textPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        textPanel.add(new JScrollPane(inputArea));
        textPanel.add(new JScrollPane(outputArea));
        add(textPanel, BorderLayout.CENTER);

        // Control Panel (Bottom)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        controlPanel.add(new JLabel("Target Language Code:"));
        controlPanel.add(languageChooser);
        controlPanel.add(translateButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Add padding around the main content
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Handles the translation button click using SwingWorker to keep the GUI responsive.
     */
    private class TranslateAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            final String inputText = inputArea.getText();
            final String targetLang = (String) languageChooser.getSelectedItem();

            outputArea.setText("Translating... Please wait for the Google Cloud API response...");
            translateButton.setEnabled(false); // Disable button during call

            // SwingWorker runs the network request in a background thread
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return TranslatorUI.getTranslation(inputText, targetLang);
                }

                @Override
                protected void done() {
                    translateButton.setEnabled(true); // Re-enable button
                    try {
                        String result = get();
                        outputArea.setText(result);
                    } catch (InterruptedException | ExecutionException ex) {
                        // Display error from the background thread
                        String errorMsg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                        outputArea.setText("Error during translation: " + errorMsg);
                        System.err.println("Error during translation: " + ex);
                    }
                }
            };

            worker.execute();
        }
    }

    /**
     * The core function, now using a custom, self-contained JSON utility for reliable parsing.
     *
     * @param text The text content to translate.
     * @param targetLang The target language code (e.g., "es" for Spanish).
     * @return The translated text, or an error message if translation fails.
     */
    public static String getTranslation(String text, String targetLang) {
        if (text == null || text.trim().isEmpty()) {
            return "Error: Cannot translate empty text.";
        }
        if (targetLang == null || !SUPPORTED_LANGUAGES.contains(targetLang)) {
            return "Error: Invalid or unsupported target language code provided: " + targetLang;
        }

        try {
            // URL Encode the text content
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());

            // Build the full URL using GET query parameters
            String fullApiUrl = String.format(
                    "%s?target=%s&key=%s&q=%s",
                    TRANSLATE_API_BASE_URL,
                    targetLang,
                    apiKey,
                    encodedText
            );

            // Setup Connection
            URL url = new URL(fullApiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Get the response code
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
            // Read lines and join them, removing newline characters
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();

            String apiResponse = response.toString();

            if (responseCode != 200) {
                return String.format("Translation API Error (%d): %s", responseCode, apiResponse);
            }

            // Parse the JSON response using the custom utility
            // Response structure: {"data": {"translations": [{"translatedText": "..."}]}}

            try {
                Map<String, Object> root = SimpleJsonParser.parse(apiResponse);

                // Navigate the structure: root -> data -> translations (List)
                if (root.containsKey("data")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> data = (Map<String, Object>) root.get("data");

                    if (data.containsKey("translations")) {
                        @SuppressWarnings("unchecked")
                        List<Object> translations = (List<Object>) data.get("translations");

                        if (!translations.isEmpty()) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> firstTranslation = (Map<String, Object>) translations.get(0);

                            // Extract the text
                            String translatedText = (String) firstTranslation.get("translatedText");

                            // Unescape common JSON characters that might be returned in the text value
                            return translatedText.replace("\\\"", "\"").replace("\\n",
                                    "\n").replace("\\/", "/");
                        }
                    }
                }

                return "Translation Failed: JSON response was successful but could not find the translation text.";

            } catch (Exception jsonEx) {
                return "Translation Failed: Failed to parse JSON response with utility. Raw Response: " + apiResponse +
                        "\nParsing Error: " + jsonEx.getMessage();
            }

        } catch (Exception e) {
            return "Translation Failed (Network/IO): " + e.getMessage();
        }
    }

    /**
     * A JSON parser to avoid external dependencies.
     * It parses the JSON string into standard Java Map and List objects.
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(TranslatorUI::new);
    }
}