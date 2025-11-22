package use_case.read_post;

import entities.OriginalPost;
import entities.ReplyPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the Read Post use case.
 */
public class ReadPostInteractor implements ReadPostInputBoundary {

    private final ReadPostDataAccessInterface postDataAccess;
    private final ReadPostOutputBoundary outputBoundary;

    public ReadPostInteractor(ReadPostDataAccessInterface postDataAccess,
                             ReadPostOutputBoundary outputBoundary) {
        this.postDataAccess = postDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ReadPostInputData inputData) {
        try {
            final OriginalPost post = (OriginalPost)postDataAccess.getPostById(inputData.getPostId());

            if (post == null) {
                outputBoundary.prepareFailView("Post not found with ID: " + inputData.getPostId());
                return;
            }

            // Convert entity to output data
            final int[] votes = post.getVotes();
            final List<ReadPostOutputData.ReplyData> replyDataList = convertReplies(post.getReplies());

            final ReadPostOutputData outputData = new ReadPostOutputData(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatorUsername(),
                    votes[0],  // upvotes
                    votes[1],  // downvotes
                    replyDataList
            );

            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to load post: " + e.getMessage());
        }
    }

    /**
     * Recursively converts ReplyPost entities to ReplyData objects.
     */
    private List<ReadPostOutputData.ReplyData> convertReplies(List<ReplyPost> replies) {
        final List<ReadPostOutputData.ReplyData> replyDataList = new ArrayList<>();

        for (ReplyPost reply : replies) {
            final int[] votes = reply.getVotes();
            final List<ReadPostOutputData.ReplyData> nestedReplies = convertReplies(reply.getReplies());

            final ReadPostOutputData.ReplyData replyData = new ReadPostOutputData.ReplyData(
                    reply.getId(),
                    reply.getCreatorUsername(),
                    reply.getContent(),
                    votes[0],  // upvotes
                    votes[1],  // downvotes
                    nestedReplies
            );

            replyDataList.add(replyData);
        }

        return replyDataList;
    }
}
