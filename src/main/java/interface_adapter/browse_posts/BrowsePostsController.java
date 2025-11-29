package interface_adapter.browse_posts;

import use_case.browse_posts.BrowsePostsInputBoundary;

/**
 * Controller for the Browse Posts use case.
 */
public class BrowsePostsController {

    private final BrowsePostsInputBoundary browsePostsInteractor;

    public BrowsePostsController(BrowsePostsInputBoundary browsePostsInteractor) {
        this.browsePostsInteractor = browsePostsInteractor;
    }

    /**
     * Executes the browse posts use case.
     */
    public void execute() {
        browsePostsInteractor.execute();
    }
}
