package use_case.read_post;

import java.util.ArrayList;
import java.util.List;

import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;

/**
 * Interactor for the Read Post use case.
 */
public class ReadPostInteractor implements ReadPostInputBoundary {

    private final ReadPostDataAccessInterface postDataAccess;
    private final ReadPostOutputBoundary outputBoundary;

    /**
     * Constructs a ReadPostInteractor.
     *
     * @param postDataAccess the data access object for posts
     * @param outputBoundary the output boundary for presenting results
     */
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
            final int[] votes = post.getVotes();
            final List<ReadPostOutputData.ReplyData> replyDataList = convertReplies(originalPost.getReplies());

            final ReadPostOutputData.ReferencedPostData referencedPostData = getReferencedPostData(originalPost);
            final List<ReadPostOutputData.ReferencingPostData> referencingPosts = findReferencingPosts(originalPost);

            final ReadPostOutputData outputData = new ReadPostOutputData(
                    originalPost.getId(),
                    originalPost.getTitle(),
                    originalPost.getContent(),
                    originalPost.getCreatorUsername(),
                    votes[0],
                    votes[1],
                    replyDataList,
                    referencedPostData,
                    referencingPosts
            );

            outputBoundary.prepareSuccessView(outputData);
        }
        catch (RuntimeException ex) {
            outputBoundary.prepareFailView("Failed to load post: " + ex.getMessage());
        }
    }

    private ReadPostOutputData.ReferencedPostData getReferencedPostData(OriginalPost originalPost) {
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
        return referencedPostData;
    }

    private List<ReadPostOutputData.ReferencingPostData> findReferencingPosts(OriginalPost originalPost) {
        final List<ReadPostOutputData.ReferencingPostData> referencingPosts = new ArrayList<>();
        try {
            final List<OriginalPost> allPosts = postDataAccess.getAllPosts();
            for (OriginalPost otherPost : allPosts) {
                if (otherPost.hasReference() && otherPost.getReferencedPost() != null) {
                    final Post referencedPost = otherPost.getReferencedPost();
                    if (referencedPost.getId() == originalPost.getId()) {
                        referencingPosts.add(new ReadPostOutputData.ReferencingPostData(
                                otherPost.getId(),
                                otherPost.getTitle(),
                                otherPost.getContent(),
                                otherPost.getCreatorUsername()
                        ));
                    }
                }
            }
        }
        catch (RuntimeException ex) {
            System.err.println("Could not load referencing posts: " + ex.getMessage());
        }
        return referencingPosts;
    }

    /**
     * Recursively converts ReplyPost entities to ReplyData objects.
     *
     * @param replies the list of reply posts
     * @return the converted list of reply data
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
                    votes[0],
                    votes[1],
                    nestedReplies
            );

            replyDataList.add(replyData);
        }

        return replyDataList;
    }
}
