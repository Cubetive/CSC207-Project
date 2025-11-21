package entities;
import java.util.Date;

public abstract class Post {

    private static long nextId = 1;
    private long id;
    private Date creation_date;
    private String creator_username;
    private String content;
    private int[] votes;

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
        this.votes = new int[]{upvotes, downvotes};
        // Update nextId to ensure it's always higher than loaded IDs
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    /**
     * Constructor for creating new posts with current date and zero votes.
     * Automatically generates a unique ID by incrementing the global counter.
     * @param creator_username the username of the post creator
     * @param content the content of the post
     */
    public Post(String creator_username, String content) {
        this(nextId++, creator_username, content, new Date(), 0, 0);
    }

    public long getId() {
        return this.id;
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

    public String getTranslation(String language) {
        // TODO
        return "";
    }

    public void upvote() {
        //TODO
    }

    public void downvote() {
       // TODO
    }

}