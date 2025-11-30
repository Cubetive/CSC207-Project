package use_case.reference_post;

import entities.OriginalPost;
import entities.Post;
import use_case.reference_post.ReferencePostOutputData.PostSearchResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The Reference Post Interactor.
 */
public class ReferencePostInteractor implements ReferencePostInputBoundary {
    private final ReferencePostDataAccessInterface dataAccess;
    private final ReferencePostOutputBoundary presenter;

    public ReferencePostInteractor(ReferencePostDataAccessInterface dataAccess,
                                  ReferencePostOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void searchPosts(ReferencePostInputData inputData) {
        final String keyword = inputData.getSearchKeyword();

        // Validate keyword is not empty
        if (keyword == null || keyword.trim().isEmpty()) {
            presenter.prepareFailView("Search keyword cannot be empty.");
            return;
        }

        // Search for posts
        final List<Post> matchingPosts = dataAccess.searchPostsByKeyword(keyword.trim());

        // Check if any posts were found
        if (matchingPosts == null || matchingPosts.isEmpty()) {
            presenter.prepareFailView("No posts found.");
            return;
        }

        // Convert posts to search results
        final List<PostSearchResult> searchResults = new ArrayList<>();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Post post : matchingPosts) {
            String title = "";
            if (post instanceof OriginalPost) {
                title = ((OriginalPost) post).getTitle();
            }

            final PostSearchResult result = new PostSearchResult(
                String.valueOf(post.getId()), // Use actual post ID
                title,
                post.getContent(),
                post.getCreatorUsername(),
                dateFormat.format(post.getCreationDate())
            );
            searchResults.add(result);
        }

        // Prepare success view with search results
        final ReferencePostOutputData outputData = new ReferencePostOutputData(
            searchResults,
            inputData.getCurrentPostId()
        );
        presenter.prepareSearchResultsView(outputData);
    }

    @Override
    public void referencePost(ReferencePostInputData inputData) {
        final String referencedPostId = inputData.getReferencedPostId();

        // Validate referenced post ID
        if (referencedPostId == null || referencedPostId.trim().isEmpty()) {
            presenter.prepareFailView("Referenced post ID cannot be empty.");
            return;
        }

        // Get the referenced post
        final Post referencedPost = dataAccess.getPostById(referencedPostId);

        if (referencedPost == null) {
            presenter.prepareFailView("Referenced post not found.");
            return;
        }

        // Get current post and attach reference
        final Post currentPost = dataAccess.getPostById(inputData.getCurrentPostId());
        
        if (currentPost == null) {
            presenter.prepareFailView("Current post not found.");
            return;
        }

        // Set the reference
        currentPost.setReferencedPost(referencedPost);

        // Save the updated post
        dataAccess.savePost(currentPost);

        // Create output data
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String title = "";
        if (referencedPost instanceof OriginalPost) {
            title = ((OriginalPost) referencedPost).getTitle();
        }

        final PostSearchResult referencedPostResult = new PostSearchResult(
            referencedPostId,
            title,
            referencedPost.getContent(),
            referencedPost.getCreatorUsername(),
            dateFormat.format(referencedPost.getCreationDate())
        );

        final ReferencePostOutputData outputData = new ReferencePostOutputData(
            referencedPostResult,
            inputData.getCurrentPostId(),
            false
        );

        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void cancelReferencePost() {
        presenter.cancelReferencePost();
    }
}
