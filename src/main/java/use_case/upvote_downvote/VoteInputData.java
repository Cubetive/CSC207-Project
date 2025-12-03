package use_case.upvote_downvote;

public class VoteInputData {
    private final boolean isUpvote;
    private final long postId;

    public VoteInputData(boolean isUpvote, long postId) {
        this.isUpvote = isUpvote;
        this.postId = postId;
    }

    public boolean isUpvote() {
        return isUpvote;
    }

    public long getPostId() {
        return postId;
    }
}
