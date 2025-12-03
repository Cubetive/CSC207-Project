package use_case.upvote_downvote;

import entities.Post;

public class VoteInteractor implements VoteInputBoundary {
    private final VoteDataAccessInterface voteDataAccessObject;
    private final VoteOutputBoundary votePresenter;

    public VoteInteractor(VoteDataAccessInterface voteDataAccessObject,
                          VoteOutputBoundary votePresenter) {
        this.voteDataAccessObject = voteDataAccessObject;
        this.votePresenter = votePresenter;
    }

    @Override
    public void execute(VoteInputData voteInputData) {
        final long id = voteInputData.getPostId();
        final boolean isUpvote = voteInputData.isUpvote();

        // 1. Fetch the post (Original, Reply, or Thread)
        final Post post = voteDataAccessObject.getPostById(id);

        if (post == null) {
            votePresenter.prepareFailView("Content with ID " + id + " not found.");
            return;
        }

        // 2. Calculate new vote counts
        int newUpvotes = post.getVotes()[0];
        int newDownvotes = post.getVotes()[1];

        if (isUpvote) {
            newUpvotes++;
        }
        else {
            if (newUpvotes - newDownvotes >= 1) {
                newDownvotes++;
            }
        }

        // 3. Save to DB/File
        voteDataAccessObject.saveVote(id, newUpvotes, newDownvotes);

        // 4. Update the Entity in memory (for immediate UI consistency)
        post.setUpvotes(newUpvotes);
        post.setDownvotes(newDownvotes);

        // 5. Output
        final VoteOutputData outputData = new VoteOutputData(id, newUpvotes, newDownvotes, false);
        votePresenter.prepareSuccessView(outputData);
    }
}
