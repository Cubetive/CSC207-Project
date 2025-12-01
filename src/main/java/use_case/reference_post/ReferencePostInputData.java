package use_case.reference_post;

/**
 * Input Data for the Reference Post Use Case.
 */
public class ReferencePostInputData {
    private final String currentPostId;
    private final String searchKeyword;
    private final String referencedPostId;

    /**
     * Constructor for searching posts.
     * @param currentPostId the ID of the current post being created
     * @param searchKeyword the keyword to search for posts
     */
    public ReferencePostInputData(String currentPostId, String searchKeyword) {
        this.currentPostId = currentPostId;
        this.searchKeyword = searchKeyword;
        this.referencedPostId = null;
    }

    /**
     * Constructor for referencing a specific post.
     * @param currentPostId the ID of the current post being created
     * @param searchKeyword the keyword used for search (can be null)
     * @param referencedPostId the ID of the post to reference
     */
    public ReferencePostInputData(String currentPostId, String searchKeyword, String referencedPostId) {
        this.currentPostId = currentPostId;
        this.searchKeyword = searchKeyword;
        this.referencedPostId = referencedPostId;
    }

    public String getCurrentPostId() {
        return currentPostId;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public String getReferencedPostId() {
        return referencedPostId;
    }
}
