package interface_adapter.translate;

/**
 * Represents the current state of the Translation view.
 * This object holds all the data required for the GUI to display and interact with the translation feature.
 * FIX: This version includes all necessary fields (including postId, statusMessage, and translationError)
 * for robust state management.
 */
public class TranslationState {

    // --- State Data ---
    // FIX: Removed inputText, as the View holds its own text content.
    private String translatedText = "";
    private String targetLanguage = "";
    // FIX: Renamed translationError to match the raw error from the Interactor/DAO.
    private String translationError = null;

    // FIX: Added postId to track the source of the translation (Post vs. Comment)
    private long postId = 0;

    // NEW fields for advanced state tracking
    private boolean isSuccessful = false;
    private boolean isFromCache = false;
    // FIX: This is the user-facing message shown in the status label.
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
        // FIX: Removed inputText copy
        this.translatedText = copy.translatedText;
        this.targetLanguage = copy.targetLanguage;
        this.translationError = copy.translationError;

        // FIX: Added postId copy
        this.postId = copy.postId;

        // Copy NEW fields
        this.isSuccessful = copy.isSuccessful;
        this.isFromCache = copy.isFromCache;
        this.statusMessage = copy.statusMessage;
    }

    // --- Getters ---
    // FIX: Removed getInputText()

    public String getTranslatedText() {
        return translatedText;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    // This holds the raw, technical error message (e.g., "400 Invalid Key")
    public String getTranslationError() {
        return translationError;
    }

    // FIX: Added Getter for postId
    public long getPostId() {
        return postId;
    }

    // NEW Getters
    public boolean isTranslationSuccessful() {
        return isSuccessful;
    }

    public boolean isFromCache() {
        return isFromCache;
    }

    // This holds the user-friendly status message (e.g., "Translation successful.")
    public String getStatusMessage() {
        return statusMessage;
    }

    // --- Setters (Used by the Presenter or Controller) ---
    // FIX: Removed setInputText()

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public void setTranslationError(String translationError) {
        this.translationError = translationError;
    }

    // FIX: Added Setter for postId
    public void setPostId(long postId) {
        this.postId = postId;
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
                ", translatedText='" + translatedText + '\'' +
                ", targetLanguage='" + targetLanguage + '\'' +
                // FIX: Used translationError and postId for complete state logging
                ", translationError='" + translationError + '\'' +
                ", postId=" + postId +
                ", isSuccessful=" + isSuccessful +
                ", isFromCache=" + isFromCache +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}