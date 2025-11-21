package use_case.search_post;

import javax.swing.DefaultListModel;

/**
 * Output Boundary for Search Post use case.
 */
public interface SearchPostOutputBoundary {

    /**
     * Prepares the success view for the Search Post use case.
     * @param listModel the list to be viewed after the search
     */
    void prepareSuccessView(DefaultListModel<String> listModel);
}
