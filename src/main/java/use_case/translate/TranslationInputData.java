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
    private final String rawText; // Nullable: only set when translating a comment/raw string

    /**
     * Constructor for translating a main post.
     * @param targetLanguage The language code to translate into.
     * @param postId The ID of the post to translate.
     */
    public TranslationInputData(String targetLanguage, long postId) {
        this.targetLanguage = targetLanguage;
        this.postId = postId;
        this.rawText = null;
    }

    /**
     * Constructor for translating raw text (e.g., a comment).
     * @param targetLanguage The language code to translate into.
     * @param rawText The raw text content to translate.
     */
    public TranslationInputData(String targetLanguage, String rawText) {
        this.targetLanguage = targetLanguage;
        this.postId = null;
        this.rawText = rawText;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public Long getPostId() {
        return postId;
    }

    public String getRawText() {
        return rawText;
    }

    /**
     * Helper method to determine if this input represents a post translation request.
     */
    public boolean isPostTranslation() {
        return postId != null;
    }
}