package interface_adapter.read_post;

import use_case.read_post.ReadPostInputBoundary;
import use_case.read_post.ReadPostInputData;

/**
 * Controller for the Read Post use case.
 */
public class ReadPostController {

    private final ReadPostInputBoundary interactor;

    public ReadPostController(ReadPostInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the read post use case for the given post title.
     * @param postTitle the title of the post to read
     */
    public void execute(String postTitle) {
        final ReadPostInputData inputData = new ReadPostInputData(postTitle);
        interactor.execute(inputData);
    }
}
