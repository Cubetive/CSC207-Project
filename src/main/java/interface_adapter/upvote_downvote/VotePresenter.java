package interface_adapter.upvote_downvote; // Ensure package matches your folder structure

import interface_adapter.read_post.ReadPostState;
import interface_adapter.read_post.ReadPostViewModel;
import use_case.read_post.ReadPostOutputData;
import use_case.upvote_downvote.VoteOutputBoundary;
import use_case.upvote_downvote.VoteOutputData;

import java.util.List;

public class VotePresenter implements VoteOutputBoundary {
    private final ReadPostViewModel readPostViewModel;

    public VotePresenter(ReadPostViewModel readPostViewModel) {
        this.readPostViewModel = readPostViewModel;
    }

    @Override
    public void prepareSuccessView(VoteOutputData outputData) {
        ReadPostState state = readPostViewModel.getState();

        // 1. Check if the vote was for the Main Post
        if (state.getId() == outputData.getId()) {
            state.setUpvotes(outputData.getNewUpvotes());
            state.setDownvotes(outputData.getNewDownvotes());
        }
        // 2. Otherwise, search through replies recursively to find and update the voted comment
        else {
            updateReplyVote(state.getReplies(), outputData);
            sortRepliesRecursive(state.getReplies());
        }

        // 3. Fire the event to refresh the View
        // The View listens to "state", so this triggers updateView() which repaints the numbers.
        readPostViewModel.setState(state);
        readPostViewModel.firePropertyChanged();
    }

    /**
     * Recursively sorts replies by (Upvotes - Downvotes) in Descending order.
     */
    private void sortRepliesRecursive(List<ReadPostOutputData.ReplyData> replies) {
        // 1. Sort the current list
        replies.sort((r1, r2) -> {
            int score1 = r1.getUpvotes() - r1.getDownvotes();
            int score2 = r2.getUpvotes() - r2.getDownvotes();
            return score2 - score1; // Descending (High -> Low)
        });

        // 2. Recursively sort nested replies
        for (ReadPostOutputData.ReplyData reply : replies) {
            if (!reply.getNestedReplies().isEmpty()) {
                sortRepliesRecursive(reply.getNestedReplies());
            }
        }
    }

    @Override
    public void prepareFailView(String error) {
        ReadPostState state = readPostViewModel.getState();
        state.setErrorMessage(error);
        readPostViewModel.firePropertyChanged();
    }

    /**
     * Recursive helper to find and update the specific reply in the view state.
     * @return true if the reply was found and updated, false otherwise.
     */
    private boolean updateReplyVote(List<ReadPostOutputData.ReplyData> replies, VoteOutputData outputData) {
        for (ReadPostOutputData.ReplyData reply : replies) {

            // A. Check if this is the reply we are looking for
            if (reply.getId() == outputData.getId()) {
                // Update the state in memory using the setters we just added
                reply.setUpvotes(outputData.getNewUpvotes());
                reply.setDownvotes(outputData.getNewDownvotes());
                return true; // Stop searching, we found it!
            }

            // B. If not, search recursively in its nested replies
            if (!reply.getNestedReplies().isEmpty()) {
                boolean foundInNested = updateReplyVote(reply.getNestedReplies(), outputData);
                if (foundInNested) {
                    return true; // Found deeper in the tree, propagate true up
                }
            }
        }
        return false; // Not found in this branch
    }
}