package interface_adapter.reply_post;

import interface_adapter.read_post.ReadPostViewModel;
import use_case.reply_post.ReplyPostOutputBoundary;
import use_case.reply_post.ReplyPostOutputData;

public class ReplyPostPresenter implements ReplyPostOutputBoundary {
    public static final String REPLY_SUCCESS = "Successful Reply";

    private final ReadPostViewModel readPostViewModel;

    public ReplyPostPresenter(ReadPostViewModel readPostViewModel) {
        this.readPostViewModel = readPostViewModel;
    }

    @Override
    public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
        readPostViewModel.getState().setErrorMessage(null);
        readPostViewModel.firePropertyChange(REPLY_SUCCESS);
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        readPostViewModel.getState().setErrorMessage(errorMessage);
        readPostViewModel.firePropertyChange();
    }
}
