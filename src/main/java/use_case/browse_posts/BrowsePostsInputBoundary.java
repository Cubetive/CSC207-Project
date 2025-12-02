package use_case.browse_posts;

/**
 * Input Boundary for the Browse Posts use case.
 */
public interface BrowsePostsInputBoundary {

    /**
     * Executes the browse posts use case.
     */
    void execute();

    /**
     * Switches to the create post view.
     */
    void switchToCreatePostView();
}
