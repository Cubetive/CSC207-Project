package interface_adapter.browse_posts;

import use_case.browse_posts.BrowsePostsOutputBoundary;
import use_case.browse_posts.BrowsePostsOutputData;

/**
 * Presenter for the Browse Posts use case.
 */
public class BrowsePostsPresenter implements BrowsePostsOutputBoundary {

    private final BrowsePostsViewModel viewModel;

    public BrowsePostsPresenter(BrowsePostsViewModel viewModel) {
        this.viewModel = viewModel;
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
}
