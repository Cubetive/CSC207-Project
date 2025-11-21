package use_case.read_post;

/**
 * Input Data for the Read Post use case.
 */
public class ReadPostInputData {

    private final long postId;

    public ReadPostInputData(long postId) {
        this.postId = postId;
    }

    public long getPostId() {
        return postId;
    }
}
