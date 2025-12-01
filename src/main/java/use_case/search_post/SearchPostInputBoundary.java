package use_case.search_post;

import interface_adapter.browse_posts.BrowsePostsState;

import javax.swing.JPanel;

/**
 * Input Boundary for Search Post use case.
 */
public interface SearchPostInputBoundary {
    /**
     * Executes the Search Post use case.
     * @param postsPanel the viewed panel before the search
     * @param state the whole post list
     * @param keyword the search bar keyword inputted
     */
    void searchPosts(JPanel postsPanel, BrowsePostsState state, String keyword);
}
