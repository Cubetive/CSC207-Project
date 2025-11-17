package entities;
import java.util.Date;

public abstract class Post {

    private Date creation_date;
    private String creator_username;
    private String content;
    private int[] votes;
    private Post referencedPost;

    public Post(String creator_username, String content) {
        this.creator_username = creator_username;
        this.content = content;
        this.creation_date = new Date();
        this.votes = new int[2];
        this.referencedPost = null;
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