package use_case.search_post;

import javax.swing.JPanel;

import interface_adapter.browse_posts.BrowsePostsState;

public class SearchPostInputData {

    private JPanel postsPanel;
    private BrowsePostsState state;
    private String keyword;

    public SearchPostInputData(JPanel postsPanel, BrowsePostsState state, String keyword) {
        this.postsPanel = postsPanel;
        this.state = state;
        this.keyword = keyword;
    }

    public JPanel getPanel() {
        return this.postsPanel;
    }
    
    public BrowsePostsState getState() {
        return this.state;
    }

    public String getKeyword() {
        return this.keyword;
    }
    
}
