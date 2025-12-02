package use_case.create_post_use_case;

public class CreatePostInputData {

    /**
     * Title of originalPost to be saved.
     */
    private final String newTitle;
    /**
     * Content of originalPost to be saved.
     */
    private final String newContent;
    /**
     * Id of referenced post, if present, of originalPost to be saved.
     */
    private final String newReferencedPostId;

    /**
     * Constructor of originalPost to be saved.
     * @param newContent content for new post.
     * @param newTitle title for new post.
     */
    public CreatePostInputData(final String newTitle, final String newContent) {
        this(newTitle, newContent, null);
    }

    /**
     * Long form constructor of originalPost to be saved.
     * @param title title for new post.
     * @param content content for new post.
     * @param referencedPostId id of referenced post, if any, for new post.
     */
    public CreatePostInputData(final String title, final String content,
                               final String referencedPostId) {
        this.newTitle = title;
        this.newContent = content;
        this.newReferencedPostId = referencedPostId;
    }

    /**
     * Gets title of originalPost to be saved.
     * @return the title of new post.
     */
    String getTitle() {
        return newTitle;
    }

    /**
     * Getter of content of originalPost to be saved.
     * @return the content of new post.
     */
    String getContent() {
        return newContent;
    }

    /**
     * Getter of referenced post of originalPost to be saved.
     * @return of new posts referenced post id as a string.
     */
    String getReferencedPostId() {
        return newReferencedPostId;
    }
}
