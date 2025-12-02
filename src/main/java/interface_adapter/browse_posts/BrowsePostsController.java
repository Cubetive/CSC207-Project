package interface_adapter.browse_posts;

import use_case.browse_posts.BrowsePostsInputBoundary;

/**
 * Controller for the Browse Posts use case.
 */
public class BrowsePostsController {

    private final BrowsePostsInputBoundary browsePostsInteractor;

    /**
     * Constructs a BrowsePostsController.
     *
     * @param browsePostsInteractor the interactor for browse posts use case
     */
    public BrowsePostsController(BrowsePostsInputBoundary browsePostsInteractor) {
        this.browsePostsInteractor = browsePostsInteractor;
    }

    /**
     * Executes the browse posts use case.
     */
    public void execute() {
        browsePostsInteractor.execute();
    }

    /**
     * Switches to the create post view.
     */
    public void switchToCreatePostView() {
        this.browsePostsInteractor.switchToCreatePostView();
    }
}
