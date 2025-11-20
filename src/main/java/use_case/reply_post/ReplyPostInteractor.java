package use_case.reply_post;

import entities.Post;
import entities.ReplyPost;

public class ReplyPostInteractor implements ReplyPostInputBoundary {
    private final ReplyPostDataAccessInterface replyPostDataAccessObject;
    private final ReplyPostOutputBoundary replyPostPresenter;

    public ReplyPostInteractor(ReplyPostDataAccessInterface replyPostDataAccessObject,
                               ReplyPostOutputBoundary replyPostPresenter) {
        this.replyPostDataAccessObject = replyPostDataAccessObject;
        this.replyPostPresenter = replyPostPresenter;
    }

    @Override
    public void execute(ReplyPostInputData replyPostInputData) {
        // The username should not be null
        final String username = replyPostInputData.getUsername();
        final String content = replyPostInputData.getContent();
        final Post parentPost = replyPostInputData.getParentPost();

        if (content.isEmpty()) {
            replyPostPresenter.prepareFailureView("Fill in missing fields.");
        }
        else {
            final ReplyPost replyPost = new ReplyPost(username, content);
            replyPostDataAccessObject.save(replyPost, parentPost);

            final ReplyPostOutputData replyPostOutputData = new ReplyPostOutputData(replyPost);
            replyPostPresenter.prepareSuccessView(replyPostOutputData);
        }
    }
}
