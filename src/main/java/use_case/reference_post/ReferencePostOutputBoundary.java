package use_case.reference_post;

/**
 * Output Boundary for the Reference Post Use Case.
 */
public interface ReferencePostOutputBoundary {

    /**
     * Prepares the success view with search results.
     * @param outputData the output data containing search results
     */
    void prepareSearchResultsView(ReferencePostOutputData outputData);

    /**
     * Prepares the success view after successfully referencing a post.
     * @param outputData the output data containing the referenced post
     */
    void prepareSuccessView(ReferencePostOutputData outputData);

    /**
     * Prepares the fail view.
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);

    /**
     * Cancels the reference post operation and returns to previous view.
     */
    void cancelReferencePost();
}
