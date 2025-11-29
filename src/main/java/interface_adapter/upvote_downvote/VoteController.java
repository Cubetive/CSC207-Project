package interface_adapter.upvote_downvote;

import use_case.upvote_downvote.VoteInputBoundary;
import use_case.upvote_downvote.VoteInputData;

/**
 * Controller for the Vote Use Case.
 * Takes input from the View (content ID and vote type) and triggers the Interactor.
 */
public class VoteController {
    final VoteInputBoundary voteInteractor;

    public VoteController(VoteInputBoundary voteInteractor) {
        this.voteInteractor = voteInteractor;
    }

    /**
     * Executes the vote action.
     *
     * @param id The ID of the post or reply being voted on.
     * @param isUpvote True for an upvote, false for a downvote.
     */
    public void execute(boolean isUpvote, long id) {
        // 1. Package the raw input data into the DTO.
        VoteInputData inputData = new VoteInputData(
                isUpvote,
                id
        );

        // 2. Delegate the execution to the Use Case Interactor.
        voteInteractor.execute(inputData);
    }
}