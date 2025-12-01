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

    public List<ReplyPost> getReplies() {
        return this.replies;
    }

    /**
     * Adds a reply to the reply list of this post object.
     * @param replyPost The reply post to be added
     */
    public void addReply(ReplyPost replyPost) {
        this.replies.add(replyPost);
    }
}
