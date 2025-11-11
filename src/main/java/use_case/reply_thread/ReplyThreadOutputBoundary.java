package use_case.reply_thread;

/**
 * Output Boundary for Reply Thread use case.
 */
public interface ReplyThreadOutputBoundary {
    /**
     * Prepares the success view for the Reply Thread use case.
     * @param replyThreadOutputData the output data
     */
    void prepareSuccessView(ReplyThreadOutputData replyThreadOutputData);

    /**
     * Prepares the failure view for the Reply Thread use case.
     * @param errorMessage explanation of the error
     */
    void prepareFailureView(String errorMessage);
}
