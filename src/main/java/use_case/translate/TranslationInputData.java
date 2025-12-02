package use_case.translate;

/**
 * Data Transfer Object (DTO) for the input of the Translation use case.
 * This DTO holds all necessary information for a translation request,
 * supporting both post translation (via postId) and raw text translation (via rawText).
 */
public class TranslationInputData {

    private final String targetLanguage;
    // Nullable: only set when translating a main post
    private final Long postId;
    private final String textContent;

    /**
     * Constructor for translating a main post. (NEW - takes all 3 parameters)
     * @param targetLanguage The language code to translate into.
     * @param postId The ID of the post to translate.
     * @param textContent The raw text content of the post to translate.
     */
    public TranslationInputData(String targetLanguage, Long postId, String textContent) {
        this.targetLanguage = targetLanguage;
        this.postId = postId;
        this.textContent = textContent;
    }

    /**
     * Constructor for translating raw text (e.g., a comment). (Original raw text constructor)
     * @param targetLanguage The language code to translate into.
     * @param textContent The raw text content to translate.
     */
    public TranslationInputData(String targetLanguage, String textContent) {
        this.targetLanguage = targetLanguage;
        this.postId = null;
        this.textContent = textContent;
    }

    /**
     * Gets the target language code.
     *
     * @return the target language code
     */
    public String getTargetLanguage() {
        return targetLanguage;
    }

    /**
     * Gets the post ID, or null if this is a raw text translation.
     *
     * @return the post ID, or null
     */
    public Long getPostId() {
        return postId;
    }

    /**
     * Gets the text content to translate.
     *
     * @return the text content
     */
    public String getTextContent() {
        return textContent;
    }

    /**
     * Determines if this input represents a post translation request.
     *
     * @return true if this is a post translation, false for raw text
     */
    public boolean isPostTranslation() {
        return postId != null;
    }
}
