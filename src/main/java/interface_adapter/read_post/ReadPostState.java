package interface_adapter.read_post;

import java.util.ArrayList;
import java.util.List;

import use_case.read_post.ReadPostOutputData;

/**
 * The state for the Read Post View.
 */
public class ReadPostState {
    private long id;
    private String title = "";
    private String content = "";
    private String username = "";
    private int upvotes;
    private int downvotes;
    private List<ReadPostOutputData.ReplyData> replies = new ArrayList<>();
    private ReadPostOutputData.ReferencedPostData referencedPost;
    private List<ReadPostOutputData.ReferencingPostData> referencingPosts = new ArrayList<>();
    private String errorMessage;

    /**
     * Gets the post ID.
     *
     * @return the post ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the post ID.
     *
     * @param id the post ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the post title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the post title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the post content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the post content.
     *
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the author's username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the author's username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the number of upvotes.
     *
     * @return the upvote count
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Sets the number of upvotes.
     *
     * @param upvotes the upvote count to set
     */
    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    /**
     * Gets the number of downvotes.
     *
     * @return the downvote count
     */
    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Sets the number of downvotes.
     *
     * @param downvotes the downvote count to set
     */
    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    /**
     * Gets the list of replies.
     *
     * @return the replies
     */
    public List<ReadPostOutputData.ReplyData> getReplies() {
        return replies;
    }

    /**
     * Sets the list of replies.
     *
     * @param replies the replies to set
     */
    public void setReplies(List<ReadPostOutputData.ReplyData> replies) {
        this.replies = replies;
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

    /**
     * Gets the referenced post data.
     *
     * @return the referenced post, or null if none
     */
    public ReadPostOutputData.ReferencedPostData getReferencedPost() {
        return referencedPost;
    }

    /**
     * Sets the referenced post data.
     *
     * @param referencedPost the referenced post to set
     */
    public void setReferencedPost(ReadPostOutputData.ReferencedPostData referencedPost) {
        this.referencedPost = referencedPost;
    }

    /**
     * Gets the list of posts that reference this post.
     *
     * @return the referencing posts
     */
    public List<ReadPostOutputData.ReferencingPostData> getReferencingPosts() {
        return referencingPosts;
    }

    /**
     * Sets the list of posts that reference this post.
     *
     * @param referencingPosts the referencing posts to set
     */
    public void setReferencingPosts(List<ReadPostOutputData.ReferencingPostData> referencingPosts) {
        this.referencingPosts = referencingPosts;
    }
}
