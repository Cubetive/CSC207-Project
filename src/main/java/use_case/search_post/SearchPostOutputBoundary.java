package use_case.search_post;

import javax.swing.JPanel;

/**
 * Output Boundary for Search Post use case.
 */
public interface SearchPostOutputBoundary {

    /**
     * Prepares the success view for the Search Post use case.
     * @param postsPanel the panel to be viewed after the search
     */
    void prepareSuccessView(JPanel postsPanel);
}
