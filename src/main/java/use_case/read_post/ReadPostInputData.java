package use_case.read_post;

/**
 * Input Data for the Read Post use case.
 */
public class ReadPostInputData {

    private final long postId;

    /**
     * Constructs a ReadPostInputData.
     *
     * @param postId the ID of the post to read
     */
    public ReadPostInputData(long postId) {
        this.postId = postId;
    }

    /**
     * Gets the post ID.
     *
     * @return the post ID
     */
    public long getPostId() {
        return postId;
    }
}
