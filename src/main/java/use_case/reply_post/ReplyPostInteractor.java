package use_case.reply_post;

import entities.OriginalPost;
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
        final long parentId = replyPostInputData.getParentId();

        if (content.isEmpty()) {
            replyPostPresenter.prepareFailureView("Fill in missing fields.");
        }
        else {
            final ReplyPost replyPost = new ReplyPost(username, content);
            final Post parentPost = replyPostDataAccessObject.getPostById(parentId);
            // Casting to either Original Posts and Reply Posts for saving purposes
            if (parentPost instanceof OriginalPost) {
                replyPostDataAccessObject.save(replyPost, (OriginalPost)parentPost);
            }
            else if (parentPost instanceof ReplyPost) {
                replyPostDataAccessObject.save(replyPost, (ReplyPost)parentPost);
            }

            final ReplyPostOutputData replyPostOutputData = new ReplyPostOutputData(replyPost);
            replyPostPresenter.prepareSuccessView(replyPostOutputData);
        }
    }
}
