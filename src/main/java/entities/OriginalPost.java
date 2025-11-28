package entities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OriginalPost extends Post {
    private final String title;
    private final List<ReplyPost> replies = new ArrayList<>();

    public OriginalPost(long id, String title, String content, String username, Date creation_date, int upvotes, int downvotes) {
        super(id, username, content, creation_date, upvotes, downvotes);
        this.title = title;
    }

    public OriginalPost(String username, String title, String content) {
        super(username, content);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<ReplyPost> getReplies() { return this.replies; }

    public void addReply(ReplyPost replyPost) {
        this.replies.add(replyPost);
    }
}