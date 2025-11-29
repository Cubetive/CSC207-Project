package use_case.reply_post;

/**
 * Output Boundary for Reply Thread use case.
 */
public interface ReplyPostOutputBoundary {
    /**
     * Prepares the success view for the Reply Thread use case.
     * @param replyPostOutputData the output data
     */
    void prepareSuccessView(ReplyPostOutputData replyPostOutputData);

    /**
     * Prepares the failure view for the Reply Thread use case.
     * @param errorMessage explanation of the error
     */
    void prepareFailureView(String errorMessage);
}
