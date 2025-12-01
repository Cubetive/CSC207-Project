package use_case.create_post_use_case;

public class CreatePostInputData {
    /**
     * title of originalPost to be saved.
     */
    private final String title;
    /**
     * content of originalPost to be saved.
     */
    private final String content;
    /**
     * id of referenced post, if present, of originalPost to be saved.
     */
    private final String referencedPostId;

    /**
     * Constructor of originalPost to be saved.
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public CreatePostInputData(final String title, final String content) {
        this(title, content, null);
    }

    /**
     * Long form constructor of originalPost to be saved.
     */
    public CreatePostInputData(final String title, final String content, final String referencedPostId) {
        this.title = title;
        this.content = content;
        this.referencedPostId = referencedPostId;
    }

    /**
     * gets title of originalPost to be saved.
     */
    String getTitle() { return title; }

    /**
     * Getter of content of originalPost to be saved.
     */
    String getContent() { return content; }

    /**
     * Getter of referenced post of originalPost to be saved.
     */
    String getReferencedPostId() { return referencedPostId; }
}
