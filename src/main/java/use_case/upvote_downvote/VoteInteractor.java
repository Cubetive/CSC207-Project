package use_case.upvote_downvote;

/**
 * The Interactor for the Vote use case.
 * Handles the business logic of updating a post or reply's vote count.
 */
public class VoteInteractor implements VoteInputBoundary {

    final VoteDataAccessInterface voteDataAccessObject;
    final VoteOutputBoundary votePresenter;

    /**
     * Constructs a VoteInteractor.
     *
     * @param voteDataAccessObject The data access object for updating vote counts.
     * @param votePresenter The presenter to handle the output result.
     */
    public VoteInteractor(VoteDataAccessInterface voteDataAccessObject,
                          VoteOutputBoundary votePresenter) {
        this.voteDataAccessObject = voteDataAccessObject;
        this.votePresenter = votePresenter;
    }

    @Override
    public void execute(VoteInputData inputData) {
        try {
            // 1. Call the DAO to update the vote count and get the result (new score and parent ID)
            VoteOutputData outputData = voteDataAccessObject.updateVoteCount(
                    inputData.getPostId(),
                    inputData.isUpvote()
            );

            // 2. Present success
            votePresenter.presentSuccess(outputData);

        } catch (RuntimeException e) {
            // 3. Present failure if the content is not found or other DAO error occurs
            votePresenter.presentFailure("Could not record vote: " + e.getMessage());
        }
    }
}