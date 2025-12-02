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
    private String translationError;
    private long postId;
    private boolean isSuccessful;
    private boolean isFromCache;
    private String statusMessage = "Ready for translation.";

    /**
     * Default constructor for TranslationState.
     */
    public TranslationState() {
    }

    /**
     * Copy constructor for TranslationState.
     * Creates a deep copy of another TranslationState object.
     *
     * @param copy the state to copy fields from
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

    /**
     * Gets the translated text.
     *
     * @return the translated text
     */
    public String getTranslatedText() {
        return translatedText;
    }

    /**
     * Gets the target language.
     *
     * @return the target language code
     */
    public String getTargetLanguage() {
        return targetLanguage;
    }

    /**
     * Gets the translation error message.
     *
     * @return the translation error, or null if no error
     */
    public String getTranslationError() {
        return translationError;
    }

    /**
     * Gets the post ID.
     *
     * @return the post ID
     */
    public long getPostId() {
        return postId;
    }

    /**
     * Checks if the translation was successful.
     *
     * @return true if successful, false otherwise
     */
    public boolean isTranslationSuccessful() {
        return isSuccessful;
    }

    /**
     * Checks if the translation was retrieved from cache.
     *
     * @return true if from cache, false otherwise
     */
    public boolean isFromCache() {
        return isFromCache;
    }

    /**
     * Gets the status message.
     *
     * @return the status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Sets the translated text.
     *
     * @param translatedText the translated text to set
     */
    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    /**
     * Sets the target language.
     *
     * @param targetLanguage the target language code to set
     */
    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    /**
     * Sets the translation error message.
     *
     * @param translationError the translation error to set
     */
    public void setTranslationError(String translationError) {
        this.translationError = translationError;
    }

    /**
     * Sets the post ID.
     *
     * @param postId the post ID to set
     */
    public void setPostId(long postId) {
        this.postId = postId;
    }

    /**
     * Sets whether the translation was successful.
     *
     * @param successful true if successful, false otherwise
     */
    public void setTranslationSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    /**
     * Sets whether the translation was retrieved from cache.
     *
     * @param fromCache true if from cache, false otherwise
     */
    public void setFromCache(boolean fromCache) {
        this.isFromCache = fromCache;
    }

    /**
     * Sets the status message.
     *
     * @param statusMessage the status message to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
