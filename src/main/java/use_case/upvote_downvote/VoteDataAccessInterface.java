package use_case.upvote_downvote;

import entities.Post; // Or a common 'Post' interface/class

public interface VoteDataAccessInterface {
    /**
     * Finds a post or reply by its ID.
     * The Interactor needs this to get the *current* upvote/downvote count.
     */
    Post getPostById(long contentId);

    /**
     * Persists the new vote counts to the storage (JSON file).
     * @param contentId The ID of the item to update.
     * @param newUpvotes The calculated new upvote total.
     * @param newDownvotes The calculated new downvote total.
     */
    void saveVote(long contentId, int newUpvotes, int newDownvotes);
}