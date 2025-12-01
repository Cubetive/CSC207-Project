package use_case.search_post;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import interface_adapter.browse_posts.BrowsePostsState;
import interface_adapter.search_post.SearchPostPresenter;
import use_case.browse_posts.BrowsePostsOutputData;

public class SearchPostInteractor implements SearchPostInputBoundary {
    
    private SearchPostOutputBoundary searchPostOutputBoundary;

    @Override
    public BrowsePostsState searchPosts(JPanel postsPanel, BrowsePostsState state, String keyword) {
        List<BrowsePostsOutputData.PostData> search_list = new ArrayList<>();
        for(int i = 0; i < state.getPosts().size(); i++) {
            if (state.getPosts().get(i).getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                search_list.add(state.getPosts().get(i));
            }
        }

        BrowsePostsState search_state = new BrowsePostsState();
        search_state.setPosts(search_list);

        SearchPostOutputData searchPostOutputData = new SearchPostOutputData(search_state);
        
        searchPostOutputBoundary = new SearchPostPresenter(searchPostOutputData);
        searchPostOutputBoundary.prepareSuccessView(postsPanel);

        return search_state;
    }

}
