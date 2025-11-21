package use_case.search_post;

import java.util.List;

import entities.OriginalPost;

public class SearchPostOutputData {
    
    private List<OriginalPost> searched_list;

    public SearchPostOutputData(List<OriginalPost> searched_list) {
        this.searched_list = searched_list;
    }

    public List<OriginalPost> getSearchedList() {
        return this.searched_list;
    }

}