package use_case.upvote_downvote;

public class VoteOutputData {
    private final int score;
    private final long postId;

    VoteOutputData(int score, long postId) {
        this.score = score;
        this.postId = postId;
    }


}
