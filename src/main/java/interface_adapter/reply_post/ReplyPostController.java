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
     * @param content the content of the reply
     * @param parentId the "parent" id of the reply "node"
     */
    public void execute(String content, long parentId) {
        final ReplyPostInputData replyPostInputData = new ReplyPostInputData(content, parentId);
        replyPostUseCaseInteractor.execute(replyPostInputData);
    }
}
