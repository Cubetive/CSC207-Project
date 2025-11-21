package use_case.read_post;

/**
 * Input Data for the Read Post use case.
 */
public class ReadPostInputData {

    private final int postId;

    public ReadPostInputData(int postId) {
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }
}
