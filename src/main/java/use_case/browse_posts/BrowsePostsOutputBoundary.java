package use_case.browse_posts;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_post.CreatePostViewModel;

/**
 * Output Boundary for the Browse Posts use case.
 */
public interface BrowsePostsOutputBoundary {

    /**
     * Prepares the success view with the retrieved posts.
     * @param outputData the output data containing the posts
     */
    void prepareSuccessView(BrowsePostsOutputData outputData);

    /**
     * Prepares the failure view.
     * @param errorMessage the error message
     */
    void prepareFailView(String errorMessage);

    void switchToCreatePostView();

    void setCreatePostViewModel(CreatePostViewModel createPostViewModel);

    void setViewManagerModel(ViewManagerModel viewManagerModel);
}
