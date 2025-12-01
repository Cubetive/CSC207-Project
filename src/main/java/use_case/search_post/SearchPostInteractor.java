package use_case.search_post;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.search_post.SearchPostPresenter;
import use_case.browse_posts.BrowsePostsOutputData;

public class SearchPostInteractor implements SearchPostInputBoundary {

    @Override
    public void searchPosts(JPanel postsPanel, BrowsePostsState state, String keyword) {
        final SearchPostOutputBoundary searchPostOutputBoundary;
        final List<BrowsePostsOutputData.PostData> searchList = new ArrayList<>();
        for (int i = 0; i < state.getPosts().size(); i++) {
            if (state.getPosts().get(i).getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                searchList.add(state.getPosts().get(i));
            }
        }

        final BrowsePostsState searchState = new BrowsePostsState();
        searchState.setPosts(searchList);

        final SearchPostOutputData searchPostOutputData = new SearchPostOutputData(searchState);
        
        searchPostOutputBoundary = new SearchPostPresenter(searchPostOutputData);
        searchPostOutputBoundary.prepareSuccessView(postsPanel);
    }

}
