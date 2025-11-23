package use_case.upvote_downvote;

public class VoteInputData {
    private final int upvotes;
    private final int downvotes;
    private final long postId;

    public VoteInputData(int upvotes, int downvotes, long postId) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.postId = postId;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public long getPostId() {
        return postId;
    }
}
