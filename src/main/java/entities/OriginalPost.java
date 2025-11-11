package entities;
import java.util.List;

public class OriginalPost extends Post {
    private String title;
    private List<ReplyPost> replies;


    public OriginalPost(String title, String content, String username) {
        // TODO
    }

    public ReplyPost replyToPost(String text, String username) {
        // TODO
        return new ReplyPost(text, username);
    }

}