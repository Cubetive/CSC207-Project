package use_case.upvote_downvote;

import entities.Post;

public class VoteInteractor implements VoteInputBoundary {
    final VoteDataAccessInterface voteDataAccessObject;
    final VoteOutputBoundary votePresenter;

    public VoteInteractor(VoteDataAccessInterface voteDataAccessObject,
                          VoteOutputBoundary votePresenter) {
        this.voteDataAccessObject = voteDataAccessObject;
        this.votePresenter = votePresenter;
    }

    @Override
    public void execute(VoteInputData voteInputData) {
        long id = voteInputData.getPostId();
        boolean isUpvote = voteInputData.isUpvote();

        // 1. Fetch the post (Original, Reply, or Thread)
        Post post = voteDataAccessObject.getPostById(id);

        if (post == null) {
            votePresenter.prepareFailView("Content with ID " + id + " not found.");
            return;
        }

        // 2. Calculate new vote counts
        int newUpvotes = post.getVotes()[0];
        int newDownvotes = post.getVotes()[1];

        if (isUpvote) {
            newUpvotes++;
        } else {
            newDownvotes++;
        }

        // 3. Save to DB/File
        voteDataAccessObject.saveVote(id, newUpvotes, newDownvotes);

        // 4. Update the Entity in memory (for immediate UI consistency)
        post.setUpvotes(newUpvotes);
        post.setDownvotes(newDownvotes);

        // 5. Output
        VoteOutputData outputData = new VoteOutputData(id, newUpvotes, newDownvotes, false);
        votePresenter.prepareSuccessView(outputData);
    }
}