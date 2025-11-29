package use_case.reference_post;

/**
 * Input Boundary for the Reference Post Use Case.
 */
public interface ReferencePostInputBoundary {

    /**
     * Executes the search for posts to reference.
     * @param referencePostInputData the input data containing search keyword
     */
    void searchPosts(ReferencePostInputData referencePostInputData);

    /**
     * Executes the reference post action.
     * @param referencePostInputData the input data containing the post to reference
     */
    void referencePost(ReferencePostInputData referencePostInputData);

    /**
     * Cancels the reference post operation.
     */
    void cancelReferencePost();
}
