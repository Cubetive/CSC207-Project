package use_case.reply_post;

/**
 * Input Boundary for Reply Thread use case.
 */
public interface ReplyPostInputBoundary {
    /**
     * Executes the Reply Thread use case.
     * @param replyPostInputData the input data
     */
    void execute(ReplyPostInputData replyPostInputData);
}
