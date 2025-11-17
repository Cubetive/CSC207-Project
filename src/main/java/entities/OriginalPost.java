package entities;
import java.util.ArrayList;
import java.util.List;

public class OriginalPost extends Post {
    private String title;
    private List<ReplyPost> replies = new ArrayList<>();


    public OriginalPost(String title, String username, String text) {
        super(username, text);
        this.title = title;
    }

    public ReplyPost replyToPost(String username, String text) {
        // TODO
        return new ReplyPost(username, text);
    }

    public String getTitle() {
        return title;
    }
}