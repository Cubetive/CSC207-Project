package use_case.search_post;

import interface_adapter.browse_posts.BrowsePostsState;

import javax.swing.JPanel;

public class SearchPostInputData {

    private JPanel postsPanel;
    private BrowsePostsState state;
    private String keyword;

    public SearchPostInputData(JPanel postsPanel, BrowsePostsState state, String keyword) {
        this.postsPanel = postsPanel;
        this.state = state;
        this.keyword = keyword;
    }

    public JPanel getJPanel() {
        return this.postsPanel;
    }
    
    public BrowsePostsState getState() {
        return this.state;
    }

    public String getKeyword() {
        return this.keyword;
    }
    
}
