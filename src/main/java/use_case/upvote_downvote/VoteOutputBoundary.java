package use_case.upvote_downvote;

public interface VoteOutputBoundary {
    /**
     * Prepares success view for voting use case.
     * @param outputData The vote output data
     */
    void prepareSuccessView(VoteOutputData outputData);

    /**
     * Prepares fail view for voting use case.
     * @param error The error message
     */
    void prepareFailView(String error);
}
