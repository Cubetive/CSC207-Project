package app;

import data_access.FilePostDataAccessObject;
import interface_adapter.browse_posts.BrowsePostsController;
import interface_adapter.browse_posts.BrowsePostsPresenter;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import use_case.browse_posts.BrowsePostsDataAccessInterface;
import use_case.browse_posts.BrowsePostsInputBoundary;
import use_case.browse_posts.BrowsePostsInteractor;
import use_case.browse_posts.BrowsePostsOutputBoundary;

/**
 * Factory for creating the Browse Posts use case.
 */
public class BrowsePostsUseCaseFactory {

    private BrowsePostsUseCaseFactory() {
        // Prevent instantiation
    }

    /**
     * Creates the Browse Posts use case.
     * @param viewModel the view model for the browse posts view
     * @param postDataAccess the data access object for posts
     * @return the browse posts controller
     */
    public static BrowsePostsController createBrowsePostsUseCase(
            BrowsePostsViewModel viewModel,
            BrowsePostsDataAccessInterface postDataAccess) {

        final BrowsePostsOutputBoundary presenter = new BrowsePostsPresenter(viewModel);
        final BrowsePostsInputBoundary interactor = new BrowsePostsInteractor(postDataAccess, presenter);

        return new BrowsePostsController(interactor);
    }
}
