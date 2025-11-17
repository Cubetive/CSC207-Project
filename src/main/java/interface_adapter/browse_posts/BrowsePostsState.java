package interface_adapter.browse_posts;

import use_case.browse_posts.BrowsePostsOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * The state for the Browse Posts View.
 */
public class BrowsePostsState {

    private List<BrowsePostsOutputData.PostData> posts = new ArrayList<>();
    private String errorMessage;

    public List<BrowsePostsOutputData.PostData> getPosts() {
        return posts;
    }

    public void setPosts(List<BrowsePostsOutputData.PostData> posts) {
        this.posts = posts;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
