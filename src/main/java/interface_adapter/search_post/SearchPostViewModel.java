package interface_adapter.search_post;

import java.util.List;

import javax.swing.DefaultListModel;

import entities.OriginalPost;

public class SearchPostViewModel {

     public void updatePostList(DefaultListModel<String> listModel, List<OriginalPost> posts) {
        listModel.clear();
        for (OriginalPost post : posts) {
            listModel.addElement(post.getTitle());
        }
    }
    
}
