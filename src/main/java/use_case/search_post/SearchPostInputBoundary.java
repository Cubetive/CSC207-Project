package use_case.search_post;

import javax.swing.JPanel;

import interface_adapter.browse_posts.BrowsePostsState;

/**
 * Input Boundary for Search Post use case.
 */
public interface SearchPostInputBoundary {
    /**
     * Executes the Search Post use case.
     * @param postsPanel the viewed post panel before the search
     * @param state the whole post list
     * @param keyword the search bar keyword inputted
     */
    void searchPosts(JPanel postsPanel, BrowsePostsState state, String keyword);
}
