package interface_adapter.search_post;

import use_case.search_post.SearchPostInputBoundary;
import use_case.search_post.SearchPostInputData;
import use_case.search_post.SearchPostInteractor;


public class SearchPostController {

    private SearchPostInputBoundary searchPostInputBoundary;
    private SearchPostInputData searchPostInputData;

    public SearchPostController(SearchPostInputData searchPostInputData) {
        this.searchPostInputData = searchPostInputData;
    }

    /**
     * Executes the Search Post Use Case.
     */
    public void searchPosts() {
        searchPostInputBoundary = new SearchPostInteractor();
        searchPostInputBoundary.searchPosts(searchPostInputData.getListModel(), searchPostInputData.getEntireOPList(), searchPostInputData.getKeyword());
    }
}
