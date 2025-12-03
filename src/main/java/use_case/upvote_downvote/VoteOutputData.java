package use_case.upvote_downvote;

public class VoteOutputData {
    private final long id;
    private final int newUpvotes;
    private final int newDownvotes;
    private final boolean useCaseFailed;

    public VoteOutputData(long id, int newUpvotes, int newDownvotes, boolean useCaseFailed) {
        this.id = id;
        this.newUpvotes = newUpvotes;
        this.newDownvotes = newDownvotes;
        this.useCaseFailed = useCaseFailed;
    }

    public long getId() {
        return id;
    }

    public int getNewUpvotes() {
        return newUpvotes;
    }

    public int getNewDownvotes() {
        return newDownvotes;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
