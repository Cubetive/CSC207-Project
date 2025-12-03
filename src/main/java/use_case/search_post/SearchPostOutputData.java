package use_case.search_post;

import interface_adapter.browse_posts.BrowsePostsState;

public class SearchPostOutputData {
    
    private BrowsePostsState state;

    public SearchPostOutputData(BrowsePostsState state) {
        this.state = state;
    }

    public BrowsePostsState getState() {
        return this.state;
    }

}
