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
            final Post post = postDataAccess.getPostById(inputData.getPostId());

            if (post == null) {
                outputBoundary.prepareFailView("Post not found with ID: " + inputData.getPostId());
                return;
            }

            // Only OriginalPosts can be read directly (replies are nested within posts)
            if (!(post instanceof OriginalPost)) {
                outputBoundary.prepareFailView("The selected item is a reply, not a post. Please select an original post.");
                return;
            }

            final OriginalPost originalPost = (OriginalPost) post;

            // Convert entity to output data
            final int[] votes = originalPost.getVotes();
            final List<ReadPostOutputData.ReplyData> replyDataList = convertReplies(originalPost.getReplies());

            final ReadPostOutputData outputData = new ReadPostOutputData(
                    originalPost.getId(),
                    originalPost.getTitle(),
                    originalPost.getContent(),
                    originalPost.getCreatorUsername(),
                    votes[0],  // upvotes
                    votes[1],  // downvotes
                    replyDataList
            );

            outputBoundary.prepareSuccessView(outputData);

        } catch (ClassCastException e) {
            outputBoundary.prepareFailView("Failed to load post: The selected item is not a valid post.");
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
