package interface_adapter.create_post;

/**
 * The state for the Create Post View Model.
 */
public class CreatePostState {
    /**
     * Current title.
     */
    private String title = "";
    /**
     * Current content.
     */
    private String content = "";
    /**
     * Current error message, if any.
     */
    private String missingError = "";
    /**
     * Current referenced post, if any.
     */
    private String referencedPostId = null; // ID of the post being referenced

    /**
     * Constructor if needed.
     */
    public CreatePostState(CreatePostState failedState) {
        this.title = failedState.title;
        this.content = failedState.content;
        this.missingError = failedState.missingError;
    }

    /**
     * Empty constructor if needed.
     */
    public CreatePostState() { }
    //Getters
    /**
     * Get current title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get current content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Get current error, if any.
     */
    public String getMissingError() {
        return missingError;
    }

    //Setters
    /**
     * Set current title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set current content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Set current error.
     */
    public void setMissingError(String missingError) {
        this.missingError = missingError;
    }

    /**
     * Get current referenced post id, if any.
     */
    public String getReferencedPostId() {
        return referencedPostId;
    }

    /**
     * Set current referenced post id.
     */
    public void setReferencedPostId(String referencedPostId) {
        this.referencedPostId = referencedPostId;
    }

}
