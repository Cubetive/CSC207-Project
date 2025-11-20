package use_case.read_post;

/**
 * Input Data for the Read Post use case.
 */
public class ReadPostInputData {

    private final String postTitle;

    public ReadPostInputData(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostTitle() {
        return postTitle;
    }
}
