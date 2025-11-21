package interface_adapter.search_post;

import javax.swing.DefaultListModel;

import use_case.search_post.SearchPostOutputBoundary;
import use_case.search_post.SearchPostOutputData;

public class SearchPostPresenter implements SearchPostOutputBoundary {
    private SearchPostOutputData searchPostOutputData;

    public SearchPostPresenter(SearchPostOutputData searchPostOutputData) {
        this.searchPostOutputData = searchPostOutputData;
    }

    @Override
    public void prepareSuccessView(DefaultListModel<String> listModel) {
        SearchPostViewModel searchPostViewModel = new SearchPostViewModel();
        searchPostViewModel.updatePostList(listModel, searchPostOutputData.getSearchedList());
    }
}
