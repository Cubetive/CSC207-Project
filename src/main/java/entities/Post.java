package entities;
import java.util.Date;

public class Post {

    private Date creation_date;
    private String creator_username;
    private String content;
    private int[] votes;

    public Date getCreationDate() {
        // TODO
        return new Date();
    }

    public void editText(String text) {
        // TODO
    }

    public int[] getVotes() {
        // TODO
        return new int[0];
    }

    public String getTranslation(String language) {
        // TODO
        return "";
    }

    public void upvote() {
        // TODO
    }

    public void downvote() {
        // TODO
    }

}