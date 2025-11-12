package entities;
import java.util.Date;

public class Post {

    private Date creation_date = new Date();
    private String creator_username;
    private String content;
    private int[] votes =  new int[2];

    public Date getCreationDate() {
        return this.creation_date;
    }

    public void editText(String text) {
        // TODO
    }

    public int[] getVotes() {
        return this.votes;
    }

    public String getTranslation(String language) {
        // TODO
        return "";
    }

    public void upvote() {
        this.votes[0]++;
    }

    public void downvote() {
        this.votes[1]++;
    }

}