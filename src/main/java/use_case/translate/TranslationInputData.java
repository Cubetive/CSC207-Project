package use_case.translate;

/**
 * Data Transfer Object (DTO) for the input of the Translation use case.
 *
 * This DTO holds all necessary information for a translation request,
 * supporting both post translation (via postId) and raw text translation (via rawText).
 */
public class TranslationInputData {

    private final String targetLanguage;
    private final Long postId; // Nullable: only set when translating a main post
    private final String textContent;

    /**
     * Constructor for translating a main post. (NEW - takes all 3 parameters)
     * @param targetLanguage The language code to translate into.
     * @param postId The ID of the post to translate.
     * @param textContent The raw text content of the post to translate.
     */
    public TranslationInputData(String targetLanguage, long postId, String textContent) {
        this.targetLanguage = targetLanguage;
        this.postId = postId;
        this.textContent = textContent; // <-- Store the content
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

    // NOTE: The previous constructor with only (targetLanguage, postId) is removed
    // because it was incomplete and bypassed caching logic in the interactor.

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public Long getPostId() {
        return postId;
    }

    public String getTextContent() {
        return textContent;
    }

    /**
     * Helper method to determine if this input represents a post translation request.
     */
    public boolean isPostTranslation() {
        return postId != null;
    }
}