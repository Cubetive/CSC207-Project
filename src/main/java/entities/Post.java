package entities;
import java.util.Date;

public abstract class Post {

    private static long nextId = 1;
    private long id;
    private Date creation_date;
    private String creator_username;
    private String content;
    private int[] votes;
    private Post referencedPost;

    /**
     * Constructor for creating new posts. Auto-generates ID and sets creation date to now.
     * @param creator_username the username of the post creator
     * @param content the content of the post
     */
    protected Post(String creator_username, String content) {
        this.id = nextId++;
        this.creator_username = creator_username;
        this.content = content;
        this.creation_date = new Date();
        this.votes = new int[2]; // [upvotes, downvotes] - both initialized to 0
        this.referencedPost = null;
    }

    /**
     * Constructor for loading posts from storage with existing creation date and votes.
     * @param id the unique identifier of the post
     * @param creator_username the username of the post creator
     * @param content the content of the post
     * @param creation_date the creation date of the post
     * @param upvotes the number of upvotes
     * @param downvotes the number of downvotes
     */
    protected Post(long id, String creator_username, String content, Date creation_date, int upvotes, int downvotes) {
        this.id = id;
        this.creator_username = creator_username;
        this.content = content;
        this.creation_date = creation_date;
        this.votes = new int[2];
        this.votes[0] = upvotes;
        this.votes[1] = downvotes;
        this.referencedPost = null;
        //Update next id.
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public Date getCreationDate() {
        return this.creation_date;
    }

    public void editText(String text) {
        this.content = text;
    }

    public int[] getVotes() {
        return this.votes;
    }

    public String getContent() {
        return this.content;
    }

    public String getCreatorUsername() {
        return this.creator_username;
    }

    public long getId() {
        return this.id;
    }

    public String getTranslation(String language) {
        // TODO
        return "";
    }

    public void setUpvotes(int upvotes) {
        this.votes[0] = upvotes;
    }

    public void setDownvotes(int downvotes) {
        this.votes[1] = downvotes;
    }

    public void upvote() {
        //TODO
    }

    public void downvote() {
       // TODO
    }

    public Post getReferencedPost() {
        return this.referencedPost;
    }

    public void setReferencedPost(Post referencedPost) {
        this.referencedPost = referencedPost;
    }

    public boolean hasReference() {
        return this.referencedPost != null;
    }

}