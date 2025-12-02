package interface_adapter.read_post;

import use_case.read_post.ReadPostInputBoundary;
import use_case.read_post.ReadPostInputData;

/**
 * Controller for the Read Post use case.
 */
public class ReadPostController {

    private final ReadPostInputBoundary interactor;

    /**
     * Constructs a ReadPostController.
     *
     * @param interactor the interactor for read post use case
     */
    public ReadPostController(ReadPostInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the read post use case for the given post ID.
     *
     * @param postId the unique identifier of the post to read
     */
    public void execute(long postId) {
        final ReadPostInputData inputData = new ReadPostInputData(postId);
        interactor.execute(inputData);
    }
}
