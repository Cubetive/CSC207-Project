package entities;
import java.util.ArrayList;
import java.util.List;

public class OriginalPost extends Post {
    private String title;
    private List<ReplyPost> replies = new ArrayList<>();


    public OriginalPost(String title, String content, String username) {
        super(username, content);
        this.title = title;
    }

    public ReplyPost replyToPost(String text, String username) {
        // TODO
        return new ReplyPost(text, username);
    }

    public String getTitle() {
        return title;
    }
}