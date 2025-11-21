package use_case.search_post;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import entities.OriginalPost;
import interface_adapter.search_post.SearchPostPresenter;

public class SearchPostInteractor implements SearchPostInputBoundary {
    
    private SearchPostOutputBoundary searchPostOutputBoundary;

    @Override
    public void searchPosts(DefaultListModel<String> listModel, List<OriginalPost> entire_op_list, String keyword) {
        List<OriginalPost> search_list = new ArrayList<OriginalPost>();
        for(int i = 0; i < entire_op_list.size(); i++) {
            if (entire_op_list.get(i).getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                search_list.add(entire_op_list.get(i));
            }
        }

        SearchPostOutputData searchPostOutputData = new SearchPostOutputData(search_list);
        
        searchPostOutputBoundary = new SearchPostPresenter(searchPostOutputData);
        searchPostOutputBoundary.prepareSuccessView(listModel);
    }

}
