package interface_adapter.translate;

/**
 * Represents the current state of the Translation view.
 * This object holds all the data required for the GUI to display and interact with the translation feature.
 */
public class TranslationState {

    // --- State Data ---
    private String inputText = "";
    private String translatedText = "";
    private String sourceLanguage = "English"; // Default starting language
    private String targetLanguage = "French";  // Default target language
    private String translationError = null; // Holds any error message

    // --- Constructor (Default) ---
    public TranslationState() {
        // Default constructor for initialization
    }

    // --- Copy Constructor (Important for clean architecture state updates) ---
    /**
     * Creates a deep copy of another TranslationState object.
     * This is crucial to ensure that state changes trigger property change events correctly.
     * @param copy The state to copy fields from.
     */
    public TranslationState(TranslationState copy) {
        this.inputText = copy.inputText;
        this.translatedText = copy.translatedText;
        this.sourceLanguage = copy.sourceLanguage;
        this.targetLanguage = copy.targetLanguage;
        this.translationError = copy.translationError;
    }

    // --- Getters ---
    public String getInputText() {
        return inputText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public String getTranslationError() {
        return translationError;
    }

    // --- Setters (Used by the Presenter or Controller) ---
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public void setTranslationError(String translationError) {
        this.translationError = translationError;
    }

    // Optional: for debugging or logging
    @Override
    public String toString() {
        return "TranslationState{" +
                "inputText='" + inputText + '\'' +
                ", translatedText='" + translatedText + '\'' +
                ", sourceLanguage='" + sourceLanguage + '\'' +
                ", targetLanguage='" + targetLanguage + '\'' +
                ", translationError='" + translationError + '\'' +
                '}';
    }
}