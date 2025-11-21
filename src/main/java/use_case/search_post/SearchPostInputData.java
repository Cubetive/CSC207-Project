package use_case.search_post;

import java.util.List;

import javax.swing.DefaultListModel;

import entities.OriginalPost;

public class SearchPostInputData {

    private DefaultListModel<String> listModel;
    private List<OriginalPost> entire_op_list;
    private String keyword;

    public SearchPostInputData(DefaultListModel<String> listModel, List<OriginalPost> entire_op_list, String keyword) {
        this.listModel = listModel;
        this.entire_op_list = entire_op_list;
        this.keyword = keyword;
    }

    public List<OriginalPost> getEntireOPList() {
        return this.entire_op_list;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public DefaultListModel<String> getListModel() {
        return this.listModel;
    }
    
}
