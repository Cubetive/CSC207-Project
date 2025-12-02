package entities;

import java.util.Date;

public abstract class Post {

    private static long nextId = 1;
    private final long id;
    private final Date creationDate;
    private final String creatorUsername;
    private String content;
    private final int[] votes;
    private Post referencedPost;

    /**
     * Constructor for creating new posts. Auto-generates ID and sets creation date to now.
     * @param creatorUsername the username of the post creator
     * @param content the content of the post
     */
    protected Post(String creatorUsername, String content) {
        this.id = nextId++;
        this.creatorUsername = creatorUsername;
        this.content = content;
        this.creationDate = new Date();
        // [upvotes, downvotes] - both initialized to 0
        this.votes = new int[2];
        this.referencedPost = null;
    }

    /**
     * Constructor for loading posts from storage with existing creation date and votes.
     * @param id the unique identifier of the post
     * @param creatorUsername the username of the post creator
     * @param content the content of the post
     * @param creationDate the creation date of the post
     * @param upvotes the number of upvotes
     * @param downvotes the number of downvotes
     */
    protected Post(long id, String creatorUsername, String content, Date creationDate, int upvotes, int downvotes) {
        this.id = id;
        this.creatorUsername = creatorUsername;
        this.content = content;
        this.creationDate = creationDate;
        this.votes = new int[2];
        this.votes[0] = upvotes;
        this.votes[1] = downvotes;
        this.referencedPost = null;
        // Update nextId to ensure it's always higher than loaded IDs
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public int[] getVotes() {
        return this.votes;
    }

    public String getContent() {
        return this.content;
    }

    public String getCreatorUsername() {
        return this.creatorUsername;
    }

    public long getId() {
        return this.id;
    }

    public void setUpvotes(int upvotes) {
        this.votes[0] = upvotes;
    }

    public void setDownvotes(int downvotes) {
        this.votes[1] = downvotes;
    }

    public Post getReferencedPost() {
        return this.referencedPost;
    }

    public void setReferencedPost(Post referencedPost) {
        this.referencedPost = referencedPost;
    }

    /**
     * Returns whether this post has a reference to another post or not.
     * @return true if post has reference, false otherwise
     */
    public boolean hasReference() {
        return this.referencedPost != null;
    }

}
