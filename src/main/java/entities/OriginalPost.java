package entities;
import java.util.ArrayList;
import java.util.List;

public class OriginalPost extends Post {
    private final String title;
    private final List<ReplyPost> replies = new ArrayList<>();


    public OriginalPost(String title, String content, String username) {
        super(username, content);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<ReplyPost> getReplies() { return this.replies; }
}