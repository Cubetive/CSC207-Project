package use_case.read_post;

import java.util.ArrayList;
import java.util.List;

/**
 * Output Data for the Read Post use case.
 */
public class ReadPostOutputData {
    private final long id; // 🔥 CRITICAL ADDITION
    private final String title;
    private final String content;
    private final String username;
    private final int upvotes;
    private final int downvotes;
    private final List<ReplyData> replies;
    private final ReferencedPostData referencedPost;
    private final List<ReferencingPostData> referencingPosts;

    public ReadPostOutputData(long id, String title, String content, String username,
                              int upvotes, int downvotes, List<ReplyData> replies) {
        this(id, title, content, username, upvotes, downvotes, replies, null, new ArrayList<>());
    }
    
    public ReadPostOutputData(long id, String title, String content, String username,
                              int upvotes, int downvotes, List<ReplyData> replies,
                              ReferencedPostData referencedPost) {
        this(id, title, content, username, upvotes, downvotes, replies, referencedPost, new ArrayList<>());
    }
    
    public ReadPostOutputData(long id, String title, String content, String username,
                              int upvotes, int downvotes, List<ReplyData> replies,
                              ReferencedPostData referencedPost, List<ReferencingPostData> referencingPosts) {
        this.id = id; // NEW
        this.title = title;
        this.content = content;
        this.username = username;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.replies = replies;
        this.referencedPost = referencedPost;
        this.referencingPosts = referencingPosts;
    }
    //NEW
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
    
    public ReferencedPostData getReferencedPost() {
        return referencedPost;
    }
    
    public List<ReferencingPostData> getReferencingPosts() {
        return referencingPosts;
    }

    /**
     * Data holder for reply information.
     */
    public static class ReplyData {
        private final long id;
        private final String username;
        private final String content;
        private int upvotes;
        private int downvotes;
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

        public void setUpvotes(int upvotes) {
            this.upvotes = upvotes;
        }

        public void setDownvotes(int downvotes) {
            this.downvotes = downvotes;
        }
    }
    
    /**
     * Data holder for referenced post information.
     */
    public static class ReferencedPostData {
        private final long id;
        private final String title;
        private final String content;
        private final String username;
        
        public ReferencedPostData(long id, String title, String content, String username) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.username = username;
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
    }
    
    /**
     * Data holder for posts that reference this post.
     */
    public static class ReferencingPostData {
        private final long id;
        private final String title;
        private final String content;
        private final String username;
        
        public ReferencingPostData(long id, String title, String content, String username) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.username = username;
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
    }
}
