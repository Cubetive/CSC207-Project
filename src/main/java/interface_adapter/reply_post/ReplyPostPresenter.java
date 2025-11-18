package interface_adapter.reply_post;

import use_case.reply_post.ReplyPostOutputBoundary;
import use_case.reply_post.ReplyPostOutputData;

public class ReplyPostPresenter implements ReplyPostOutputBoundary {
    // TODO: Implement the Presenter

    @Override
    public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {

    }

    @Override
    public void prepareFailureView(String errorMessage) {

    }
}
