package interface_adapter.browse_posts;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_post.CreatePostState;
import interface_adapter.create_post.CreatePostViewModel;
import use_case.browse_posts.BrowsePostsOutputBoundary;
import use_case.browse_posts.BrowsePostsOutputData;

/**
 * Presenter for the Browse Posts use case.
 */
public class BrowsePostsPresenter implements BrowsePostsOutputBoundary {

    private final BrowsePostsViewModel viewModel;
    private CreatePostViewModel createPostViewModel;
    private ViewManagerModel viewManagerModel;

    /**
     * Constructs a BrowsePostsPresenter.
     *
     * @param viewModel the view model for browse posts
     */
    public BrowsePostsPresenter(BrowsePostsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void setCreatePostViewModel(CreatePostViewModel createPostViewModel) {
        this.createPostViewModel = createPostViewModel;
    }

    @Override
    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(BrowsePostsOutputData outputData) {
        final BrowsePostsState state = viewModel.getState();
        state.setPosts(outputData.getPosts());
        state.setErrorMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final BrowsePostsState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void switchToCreatePostView() {
        createPostViewModel.setState(new CreatePostState());
        this.viewManagerModel.setState(createPostViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
