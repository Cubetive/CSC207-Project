package use_case.browse_posts;

/**
 * Output Boundary for the Browse Posts use case.
 */
public interface BrowsePostsOutputBoundary {

    /**
     * Prepares the success view with the retrieved posts.
     * @param outputData the output data containing the posts
     */
    void prepareSuccessView(BrowsePostsOutputData outputData);

    /**
     * Prepares the failure view.
     * @param errorMessage the error message
     */
    void prepareFailView(String errorMessage);
}
