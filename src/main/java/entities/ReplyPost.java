package entities;
import java.util.ArrayList;
import java.util.List;

public class ReplyPost extends Post {

    private final List<ReplyPost> replies = new ArrayList<>();

    public ReplyPost(String username, String content) {
        super(username, content);
    }

    public List<ReplyPost> getReplies() { return this.replies; }
}