package use_case.read_post;

/**
 * Output Boundary for the Read Post use case.
 */
public interface ReadPostOutputBoundary {

    /**
     * Prepares the success view with the post data.
     * @param outputData the output data containing the post and its replies
     */
    void prepareSuccessView(ReadPostOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);
}
