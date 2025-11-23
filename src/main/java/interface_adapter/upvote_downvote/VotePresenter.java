package interface_adapter.upvote_downvote;

import use_case.upvote_downvote.VoteOutputBoundary;
import use_case.upvote_downvote.VoteOutputData;
import interface_adapter.ViewManagerModel;

/**
 * Presenter for the Vote Use Case.
 * Updates the VoteViewModel and potentially the ViewManagerModel (e.g., to re-render the ReadPostView).
 */
public class VotePresenter implements VoteOutputBoundary {

    private final VoteViewModel voteViewModel;
    private final ViewManagerModel viewManagerModel;

    public VotePresenter(VoteViewModel voteViewModel, ViewManagerModel viewManagerModel) {
        this.voteViewModel = voteViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Handles a successful vote.
     * The key action here is to trigger the ReadPostView to re-read the post
     * using the parentPostId so that replies are re-sorted and re-displayed.
     *
     * @param outputData The data object containing the new score and parent post ID.
     */
    @Override
    public void presentSuccess(VoteOutputData outputData) {
        // 1. Update the VoteState (primarily for the parentPostId)
        VoteState voteState = voteViewModel.getState();
        voteState.setNewVoteScore(outputData.getNewScore());
        voteState.setParentPostId(outputData.getPostId());
        voteState.setVoteError(null);

        // 2. Trigger the VoteViewModel to notify any listeners (like the PostReadingView)
        this.voteViewModel.setState(voteState);
        this.voteViewModel.firePropertyChanged();
    }

    /**
     * Handles a failed vote attempt.
     * @param errorMessage A descriptive error message.
     */
    @Override
    public void presentFailure(String errorMessage) {
        VoteState voteState = voteViewModel.getState();
        voteState.setVoteError(errorMessage);

        this.voteViewModel.setState(voteState);
        this.voteViewModel.firePropertyChanged();
    }
}