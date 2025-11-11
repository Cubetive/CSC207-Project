package use_case.reply_thread;

/**
 * Input Boundary for Reply Thread use case.
 */
public interface ReplyThreadInputBoundary {
    /**
     * Executes the Reply Thread use case.
     * @param replyThreadInputData the input data
     */
    void execute(ReplyThreadInputData replyThreadInputData);
}
