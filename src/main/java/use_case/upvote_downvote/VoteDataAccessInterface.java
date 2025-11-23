package use_case.upvote_downvote;

public interface VoteDataAccessInterface {
    /**
     * Updates the vote count for a piece of content and returns its new score and parent post ID.
     *
     * @param contentId The ID of the post or reply.
     * @param isUpvote True for an upvote, false for a downvote.
     * @return VoteOutputData containing the new score and the parent post ID.
     * @throws RuntimeException if the content is not found.
     */
    VoteOutputData updateVoteCount(long contentId, boolean isUpvote);
}
