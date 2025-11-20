package interface_adapter.reference_post;

import use_case.reference_post.ReferencePostOutputData.PostSearchResult;
import java.util.List;

/**
 * State for the Reference Post View Model.
 */
public class ReferencePostState {
    private String currentPostId = "";
    private String searchKeyword = "";
    private List<PostSearchResult> searchResults = null;
    private PostSearchResult referencedPost = null;
    private String errorMessage = null;

    public ReferencePostState(ReferencePostState copy) {
        this.currentPostId = copy.currentPostId;
        this.searchKeyword = copy.searchKeyword;
        this.searchResults = copy.searchResults;
        this.referencedPost = copy.referencedPost;
        this.errorMessage = copy.errorMessage;
    }

    public ReferencePostState() {
    }

    public String getCurrentPostId() {
        return currentPostId;
    }

    public void setCurrentPostId(String currentPostId) {
        this.currentPostId = currentPostId;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public List<PostSearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<PostSearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public PostSearchResult getReferencedPost() {
        return referencedPost;
    }

    public void setReferencedPost(PostSearchResult referencedPost) {
        this.referencedPost = referencedPost;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
