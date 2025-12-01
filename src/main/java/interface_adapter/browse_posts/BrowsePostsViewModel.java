package interface_adapter.browse_posts;

import interface_adapter.ViewModel;

/**
 * The View Model for the Browse Posts View.
 */
public class BrowsePostsViewModel extends ViewModel<BrowsePostsState> {

    public static final String TITLE_LABEL = "Browse Posts";
    public static final String REFRESH_BUTTON_LABEL = "Refresh";

    public BrowsePostsViewModel() {
        super("browse posts");
        setState(new BrowsePostsState());
    }
}
