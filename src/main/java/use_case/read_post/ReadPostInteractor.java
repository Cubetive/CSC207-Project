package use_case.read_post;

import entities.OriginalPost;
import entities.Post;
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

            final OriginalPost originalPost = (OriginalPost) post;

            // Convert entity to output data
            final int[] votes = post.getVotes();
            final List<ReadPostOutputData.ReplyData> replyDataList = convertReplies(originalPost.getReplies());
            
            // Get referenced post if it exists
            ReadPostOutputData.ReferencedPostData referencedPostData = null;
            if (originalPost.hasReference()) {
                final Post referencedPost = originalPost.getReferencedPost();
                String referencedTitle = "";
                if (referencedPost instanceof OriginalPost) {
                    referencedTitle = ((OriginalPost) referencedPost).getTitle();
                }
                referencedPostData = new ReadPostOutputData.ReferencedPostData(
                        referencedPost.getId(),
                        referencedTitle,
                        referencedPost.getContent(),
                        referencedPost.getCreatorUsername()
                );
            }
            
            // Find posts that reference this post
            final List<ReadPostOutputData.ReferencingPostData> referencingPosts = new ArrayList<>();
            try {
                final List<OriginalPost> allPosts = postDataAccess.getAllPosts();
                for (OriginalPost otherPost : allPosts) {
                    if (otherPost.hasReference() && otherPost.getReferencedPost() != null) {
                        final Post referencedPost = otherPost.getReferencedPost();
                        if (referencedPost.getId() == originalPost.getId()) {
                            // This post references the current post
                            referencingPosts.add(new ReadPostOutputData.ReferencingPostData(
                                    otherPost.getId(),
                                    otherPost.getTitle(),
                                    otherPost.getContent(),
                                    otherPost.getCreatorUsername()
                            ));
                        }
                    }
                }
            } catch (Exception e) {
                // If getAllPosts is not available, just continue without referencing posts
                System.err.println("Could not load referencing posts: " + e.getMessage());
            }

            final ReadPostOutputData outputData = new ReadPostOutputData(
                    originalPost.getId(),
                    originalPost.getTitle(),
                    originalPost.getContent(),
                    originalPost.getCreatorUsername(),
                    votes[0],  // upvotes
                    votes[1],  // downvotes
                    replyDataList,
                    referencedPostData,
                    referencingPosts
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
