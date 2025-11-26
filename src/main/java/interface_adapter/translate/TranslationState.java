package interface_adapter.translate;

/**
 * Represents the current state of the Translation view.
 * This object holds all the data required for the GUI to display and interact with the translation feature.
 * FIX: This version includes all necessary fields (including postId, statusMessage, and translationError)
 * for robust state management.
 */
public class TranslationState {

    private String translatedText = "";
    private String targetLanguage = "";
    private String translationError = null;

    // Added postId to track the source of the translation (Post vs. Comment)
    private long postId = 0;


    private boolean isSuccessful = false;
    private boolean isFromCache = false;

    private String statusMessage = "Ready for translation.";

    // --- Constructor (Default) ---
    public TranslationState() {
        // Default constructor for initialization
    }

    // --- Copy Constructor (for clean architecture state updates) ---
    /**
     * Creates a deep copy of another TranslationState object.
     * This is crucial to ensure that state changes trigger property change events correctly.
     * @param copy The state to copy fields from.
     */
    public TranslationState(TranslationState copy) {
        this.translatedText = copy.translatedText;
        this.targetLanguage = copy.targetLanguage;
        this.translationError = copy.translationError;

        this.postId = copy.postId;

        this.isSuccessful = copy.isSuccessful;
        this.isFromCache = copy.isFromCache;
        this.statusMessage = copy.statusMessage;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public String getTranslationError() {
        return translationError;
    }

    public long getPostId() {
        return postId;
    }

    public boolean isTranslationSuccessful() {
        return isSuccessful;
    }

    public boolean isFromCache() {
        return isFromCache;
    }

    public String getStatusMessage() {
        return statusMessage;
    }


    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public void setTranslationError(String translationError) {
        this.translationError = translationError;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setTranslationSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public void setFromCache(boolean fromCache) {
        this.isFromCache = fromCache;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}