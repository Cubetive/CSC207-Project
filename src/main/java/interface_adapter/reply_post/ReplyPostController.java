package interface_adapter.reply_post;

import use_case.reply_post.ReplyPostInputBoundary;
import use_case.reply_post.ReplyPostInputData;

public class ReplyPostController {
    private final ReplyPostInputBoundary replyPostUseCaseInteractor;

    public ReplyPostController(ReplyPostInputBoundary replyPostUseCaseInteractor) {
        this.replyPostUseCaseInteractor = replyPostUseCaseInteractor;
    }

    /**
     * Executes the Reply Post Use Case
     * @param username the username of the replier
     * @param content the content of the reply
     */
    public void execute(String username, String content) {
        final ReplyPostInputData replyPostInputData = new ReplyPostInputData(username, content);
        replyPostUseCaseInteractor.execute(replyPostInputData);
    }
}
