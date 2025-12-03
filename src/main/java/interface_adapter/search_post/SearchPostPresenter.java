package interface_adapter.search_post;

import javax.swing.JPanel;

import use_case.search_post.SearchPostOutputBoundary;
import use_case.search_post.SearchPostOutputData;

public class SearchPostPresenter implements SearchPostOutputBoundary {
    private SearchPostOutputData searchPostOutputData;

    public SearchPostPresenter(SearchPostOutputData searchPostOutputData) {
        this.searchPostOutputData = searchPostOutputData;
    }

    @Override
    public void prepareSuccessView(JPanel postsPanel) {
        final SearchPostViewModel searchPostViewModel = new SearchPostViewModel();
        searchPostViewModel.updatePostList(postsPanel, searchPostOutputData.getState());
    }
}
