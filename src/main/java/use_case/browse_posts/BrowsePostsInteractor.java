package use_case.browse_posts;

import java.util.ArrayList;
import java.util.List;

import entities.OriginalPost;
import entities.Post;

/**
 * Interactor for the Browse Posts use case.
 */
public class BrowsePostsInteractor implements BrowsePostsInputBoundary {

    private static final int CONTENT_PREVIEW_LENGTH = 50;

    private final BrowsePostsDataAccessInterface postDataAccess;
    private final BrowsePostsOutputBoundary outputBoundary;

    /**
     * Constructs a BrowsePostsInteractor.
     *
     * @param postDataAccess the data access object for posts
     * @param outputBoundary the output boundary for presenting results
     */
    public BrowsePostsInteractor(
            BrowsePostsDataAccessInterface postDataAccess,
            BrowsePostsOutputBoundary outputBoundary) {
        this.postDataAccess = postDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute() {
        try {
            final List<OriginalPost> posts = postDataAccess.getAllPosts();
            posts.sort((firstPost, secondPost) -> {
                final int scoreOne = firstPost.getVotes()[0] - firstPost.getVotes()[1];
                final int scoreTwo = secondPost.getVotes()[0] - secondPost.getVotes()[1];
                return scoreTwo - scoreOne;
            });
            final List<BrowsePostsOutputData.PostData> postDataList = getPostData(posts);
            final BrowsePostsOutputData outputData = new BrowsePostsOutputData(postDataList);
            outputBoundary.prepareSuccessView(outputData);
        }
        catch (RuntimeException ex) {
            outputBoundary.prepareFailView("Failed to load posts: " + ex.getMessage());
        }
    }

    private static List<BrowsePostsOutputData.PostData> getPostData(List<OriginalPost> posts) {
        final List<BrowsePostsOutputData.PostData> postDataList = new ArrayList<>();
        for (OriginalPost post : posts) {
            final int[] votes = post.getVotes();

            final boolean hasReference = post.hasReference();
            String referencedPostTitle = null;
            Long referencedPostId = null;
            if (hasReference && post.getReferencedPost() != null) {
                final Post referencedPost = post.getReferencedPost();
                referencedPostId = referencedPost.getId();
                if (referencedPost instanceof OriginalPost) {
                    referencedPostTitle = ((OriginalPost) referencedPost).getTitle();
                }
                else {
                    final String content = referencedPost.getContent();
                    referencedPostTitle = content.length() > CONTENT_PREVIEW_LENGTH
                            ? content.substring(0, CONTENT_PREVIEW_LENGTH) + "..."
                            : content;
                }
            }

            final BrowsePostsOutputData.PostData postData = new BrowsePostsOutputData.PostData(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatorUsername(),
                    post.getCreationDate(),
                    votes[0],
                    votes[1],
                    hasReference,
                    referencedPostTitle,
                    referencedPostId
            );
            postDataList.add(postData);
        }
        return postDataList;
    }

    @Override
    public void switchToCreatePostView() {
        this.outputBoundary.switchToCreatePostView();
    }
}
