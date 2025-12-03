package use_case.upvote_downvote;

public interface VoteInputBoundary {
    /**
     * Executes voting use case.
     * @param inputData The input vote data
     */
    void execute(VoteInputData inputData);
}
