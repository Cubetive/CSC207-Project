package use_case.search_post;

import java.util.List;

import javax.swing.DefaultListModel;

import entities.OriginalPost;

/**
 * Input Boundary for Search Post use case.
 */
public interface SearchPostInputBoundary {
    /**
     * Executes the Search Post use case.
     * @param listModel the viewed list before the search
     * @param entire_op_list the whole OriginalPost list
     * @param keyword the search bar keyword inputted
     */
    void searchPosts(DefaultListModel<String> listModel, List<OriginalPost> entire_op_list, String keyword);
}
