package use_case.upvote_downvote;

public interface VoteOutputBoundary {
    void presentSuccess(VoteOutputData outputData);
    void presentFailure(String errorMessage);
}
