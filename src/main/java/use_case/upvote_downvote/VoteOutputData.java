package use_case.upvote_downvote;

public class VoteOutputData {
    private final int newScore;
    private final long postId;

    public VoteOutputData(int newScore, long postId) {
        this.newScore = newScore;
        this.postId = postId;
    }

    public int getNewScore() {
        return newScore;
    }
    public long getPostId() {
        return postId;
    }

}
