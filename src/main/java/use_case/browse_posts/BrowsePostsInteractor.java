package use_case.browse_posts;

import entities.OriginalPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the Browse Posts use case.
 */
public class BrowsePostsInteractor implements BrowsePostsInputBoundary {

    private final BrowsePostsDataAccessInterface postDataAccess;
    private final BrowsePostsOutputBoundary outputBoundary;

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

            // Convert entity posts to output data
            final List<BrowsePostsOutputData.PostData> postDataList = new ArrayList<>();
            for (OriginalPost post : posts) {
                final BrowsePostsOutputData.PostData postData = new BrowsePostsOutputData.PostData(
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatorUsername()
                );
                postDataList.add(postData);
            }

            final BrowsePostsOutputData outputData = new BrowsePostsOutputData(postDataList);
            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to load posts: " + e.getMessage());
        }
    }
}
