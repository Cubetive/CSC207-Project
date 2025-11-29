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
        System.out.println("INTERACTOR DEBUG: BrowsePostsInteractor execute called."); // 🔥 CHECK 1
        try {
            final List<OriginalPost> posts = postDataAccess.getAllPosts();
            System.out.println("INTERACTOR DEBUG: DAO returned " + posts.size() + " posts."); // 🔥 CHECK 2

            // Convert entity posts to output data
            final List<BrowsePostsOutputData.PostData> postDataList = getPostData(posts);
            System.out.println("INTERACTOR DEBUG: Converted to " + postDataList.size() + " output objects."); // 🔥 CHECK 3

            final BrowsePostsOutputData outputData = new BrowsePostsOutputData(postDataList);
            outputBoundary.prepareSuccessView(outputData);
            System.out.println("INTERACTOR DEBUG: Sent success to Presenter."); // 🔥 CHECK 4

        } catch (Exception e) {
            System.err.println("INTERACTOR CRASH: " + e.getMessage()); // 🔥 CHECK FAILURE
            e.printStackTrace();
            outputBoundary.prepareFailView("Failed to load posts: " + e.getMessage());
        }
    }

    private static List<BrowsePostsOutputData.PostData> getPostData(List<OriginalPost> posts) {
        final List<BrowsePostsOutputData.PostData> postDataList = new ArrayList<>();
        for (OriginalPost post : posts) {
            final int[] votes = post.getVotes();
            final BrowsePostsOutputData.PostData postData = new BrowsePostsOutputData.PostData(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatorUsername(),
                    post.getCreationDate(),
                    votes[0],  // upvotes
                    votes[1]   // downvotes
            );
            postDataList.add(postData);
        }
        return postDataList;
    }
}
