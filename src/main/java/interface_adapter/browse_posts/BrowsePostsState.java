package interface_adapter.browse_posts;

import java.util.ArrayList;
import java.util.List;

import use_case.browse_posts.BrowsePostsOutputData;

/**
 * The state for the Browse Posts View.
 */
public class BrowsePostsState {

    private List<BrowsePostsOutputData.PostData> posts = new ArrayList<>();
    private String errorMessage;

    /**
     * Gets the list of posts.
     *
     * @return the posts
     */
    public List<BrowsePostsOutputData.PostData> getPosts() {
        return posts;
    }

    /**
     * Sets the list of posts.
     *
     * @param posts the posts to set
     */
    public void setPosts(List<BrowsePostsOutputData.PostData> posts) {
        this.posts = posts;
    }

    /**
     * Gets the error message.
     *
     * @return the error message, or null if no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message.
     *
     * @param errorMessage the error message to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
