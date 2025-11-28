package use_case.upvote_downvote;

public interface VoteOutputBoundary {
    void prepareSuccessView(VoteOutputData outputData);
    void prepareFailView(String error);
}