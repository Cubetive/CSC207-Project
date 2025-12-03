package interface_adapter.upvote_downvote;

/**
 * State object for the Vote Use Case.
 * Contains information about the result of a vote action, primarily used to trigger a view update.
 */
public class VoteState {
    private long updatedId = -1;
    private int newVoteScore;
    private long parentPostId = -1;
    private String voteError;

    // Default constructor
    public VoteState() {

    }

    // Copy constructor
    public VoteState(VoteState copy) {
        this.updatedId = copy.updatedId;
        this.newVoteScore = copy.newVoteScore;
        this.parentPostId = copy.parentPostId;
        this.voteError = copy.voteError;
    }

    // --- Getters ---
    public long getUpdatedId() {
        return updatedId;
    }

    public int getNewVoteScore() {
        return newVoteScore;
    }

    public long getParentPostId() {
        return parentPostId;
    }

    public String getVoteError() {
        return voteError;
    }

    // --- Setters ---
    public void setUpdatedId(long updatedId) {
        this.updatedId = updatedId;
    }

    public void setNewVoteScore(int newVoteScore) {
        this.newVoteScore = newVoteScore;
    }

    public void setParentPostId(long parentPostId) {
        this.parentPostId = parentPostId;
    }

    public void setVoteError(String voteError) {
        this.voteError = voteError;
    }
}
