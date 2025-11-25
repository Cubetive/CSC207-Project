package use_case.read_post;

import java.util.List;

/**
 * Output Data for the Read Post use case.
 */
public class ReadPostOutputData {
    private final long id; //NEW
    private final String title;
    private final String content;
    private final String username;
    private final int upvotes;
    private final int downvotes;
    private final List<ReplyData> replies;

    public ReadPostOutputData(long id, String title, String content, String username,
                              int upvotes, int downvotes, List<ReplyData> replies) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.replies = replies;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public List<ReplyData> getReplies() {
        return replies;
    }

    /**
     * Data holder for reply information.
     */
    public static class ReplyData {
        private final long id; //NEW
        private final String username;
        private final String content;
        private final int upvotes;
        private final int downvotes;
        private final List<ReplyData> nestedReplies;

        public ReplyData(long id, String username, String content, int upvotes, int downvotes,
                        List<ReplyData> nestedReplies) {
            this.id = id;
            this.username = username;
            this.content = content;
            this.upvotes = upvotes;
            this.downvotes = downvotes;
            this.nestedReplies = nestedReplies;
        }

        public long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getContent() {
            return content;
        }

        public int getUpvotes() {
            return upvotes;
        }

        public int getDownvotes() {
            return downvotes;
        }

        public List<ReplyData> getNestedReplies() {
            return nestedReplies;
        }
    }
}
