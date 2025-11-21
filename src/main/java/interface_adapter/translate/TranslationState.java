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

    // NEW fields for advanced state tracking
    private boolean isSuccessful = false;
    private boolean isFromCache = false;
    private String statusMessage = "Ready for translation.";

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

        // Copy NEW fields
        this.isSuccessful = copy.isSuccessful;
        this.isFromCache = copy.isFromCache;
        this.statusMessage = copy.statusMessage;
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

    // NEW Getters
    public boolean isTranslationSuccessful() {
        // We consider it successful if the explicit flag is set, or if an error message is absent
        // (though relying on the explicit flag is better practice for the presenter).
        return isSuccessful;
    }

    public boolean isFromCache() {
        return isFromCache;
    }

    public String getStatusMessage() {
        return statusMessage;
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

    // NEW Setters
    public void setTranslationSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public void setFromCache(boolean fromCache) {
        this.isFromCache = fromCache;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
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
                ", isSuccessful=" + isSuccessful +
                ", isFromCache=" + isFromCache +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}