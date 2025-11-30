package interface_adapter.read_post;

import use_case.read_post.ReadPostOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * The state for the Read Post View.
 */
public class ReadPostState {

    private long id = 0;
    private String title = "";
    private String content = "";
    private String username = "";
    private int upvotes = 0;
    private int downvotes = 0;
    private List<ReadPostOutputData.ReplyData> replies = new ArrayList<>();
    private String errorMessage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public List<ReadPostOutputData.ReplyData> getReplies() {
        return replies;
    }

    public void setReplies(List<ReadPostOutputData.ReplyData> replies) {
        this.replies = replies;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
