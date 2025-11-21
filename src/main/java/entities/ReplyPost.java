package entities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReplyPost extends Post {

    private final List<ReplyPost> replies = new ArrayList<>();

    public ReplyPost(long id, String username, String content, Date creation_date, int upvotes, int downvotes) {
        super(id, username, content, creation_date, upvotes, downvotes);
    }

    public ReplyPost(String username, String content) {
        super(username, content);
    }

    public ReplyPost replyToPost(String username, String content) {
        ReplyPost replyPost = new ReplyPost(username, content);
        replies.add(replyPost);
        return replyPost;
    }

    public List<ReplyPost> getReplies() { return this.replies; }
}