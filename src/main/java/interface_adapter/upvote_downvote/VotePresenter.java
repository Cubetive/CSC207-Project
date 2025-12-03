package interface_adapter.upvote_downvote;

import java.util.List;

import interface_adapter.read_post.ReadPostState;
import interface_adapter.read_post.ReadPostViewModel;
import use_case.read_post.ReadPostOutputData;
import use_case.upvote_downvote.VoteOutputBoundary;
import use_case.upvote_downvote.VoteOutputData;

public class VotePresenter implements VoteOutputBoundary {
    private final ReadPostViewModel readPostViewModel;

    public VotePresenter(ReadPostViewModel readPostViewModel) {
        this.readPostViewModel = readPostViewModel;
    }

    @Override
    public void prepareSuccessView(VoteOutputData outputData) {
        final ReadPostState state = readPostViewModel.getState();

        if (state.getId() == outputData.getId()) {
            state.setUpvotes(outputData.getNewUpvotes());
            state.setDownvotes(outputData.getNewDownvotes());
        }

        else {
            updateReplyVote(state.getReplies(), outputData);
            sortRepliesRecursive(state.getReplies());
        }

        readPostViewModel.setState(state);
        readPostViewModel.firePropertyChanged();
    }

    /**
     * Recursively sorts replies by (Upvotes - Downvotes) in Descending order.
     * @param replies The replies of the post
     */
    private void sortRepliesRecursive(List<ReadPostOutputData.ReplyData> replies) {

        replies.sort((one, two) -> {
            final int score1 = one.getUpvotes() - one.getDownvotes();
            final int score2 = two.getUpvotes() - two.getDownvotes();
            return score2 - score1;
        });

        for (ReadPostOutputData.ReplyData reply : replies) {
            if (!reply.getNestedReplies().isEmpty()) {
                sortRepliesRecursive(reply.getNestedReplies());
            }
        }
    }

    @Override
    public void prepareFailView(String error) {
        final ReadPostState state = readPostViewModel.getState();
        state.setErrorMessage(error);
        readPostViewModel.firePropertyChanged();
    }

    /**
     * Recursive helper to find and update the specific reply in the view state.
     * @param replies The replies of the post
     * @param outputData The voting output data
     * @return true if the reply was found and updated, false otherwise.
     */
    private boolean updateReplyVote(List<ReadPostOutputData.ReplyData> replies, VoteOutputData outputData) {
        for (ReadPostOutputData.ReplyData reply : replies) {

            if (reply.getId() == outputData.getId()) {

                reply.setUpvotes(outputData.getNewUpvotes());
                reply.setDownvotes(outputData.getNewDownvotes());
                return true;
            }

            if (!reply.getNestedReplies().isEmpty()) {
                final boolean foundInNested = updateReplyVote(reply.getNestedReplies(), outputData);
                if (foundInNested) {
                    return true;
                }
            }
        }
        return false;
    }
}
