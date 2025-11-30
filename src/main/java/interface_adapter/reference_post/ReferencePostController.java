package interface_adapter.reference_post;

import use_case.reference_post.ReferencePostInputBoundary;
import use_case.reference_post.ReferencePostInputData;

/**
 * Controller for the Reference Post Use Case.
 */
public class ReferencePostController {

    private final ReferencePostInputBoundary referencePostInteractor;

    public ReferencePostController(ReferencePostInputBoundary referencePostInteractor) {
        this.referencePostInteractor = referencePostInteractor;
    }

    /**
     * Executes the search for posts to reference.
     * @param currentPostId the ID of the current post being created
     * @param searchKeyword the keyword to search for
     */
    public void searchPosts(String currentPostId, String searchKeyword) {
        final ReferencePostInputData inputData = new ReferencePostInputData(
            currentPostId,
            searchKeyword
        );
        referencePostInteractor.searchPosts(inputData);
    }

    /**
     * Executes the reference post action.
     * @param currentPostId the ID of the current post being created
     * @param referencedPostId the ID of the post to reference
     */
    public void referencePost(String currentPostId, String referencedPostId) {
        final ReferencePostInputData inputData = new ReferencePostInputData(
            currentPostId,
            null,
            referencedPostId
        );
        referencePostInteractor.referencePost(inputData);
    }

    /**
     * Cancels the reference post operation.
     */
    public void cancelReferencePost() {
        referencePostInteractor.cancelReferencePost();
    }
}
