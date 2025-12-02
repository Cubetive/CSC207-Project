package use_case.read_post;

import java.util.ArrayList;
import java.util.List;

/**
 * Output Data for the Read Post use case.
 */
public class ReadPostOutputData {
    private final long id;
    private final String title;
    private final String content;
    private final String username;
    private final int upvotes;
    private final int downvotes;
    private final List<ReplyData> replies;
    private final ReferencedPostData referencedPost;
    private final List<ReferencingPostData> referencingPosts;

    /**
     * Constructs output data without reference information.
     *
     * @param id the post ID
     * @param title the post title
     * @param content the post content
     * @param username the author's username
     * @param upvotes the number of upvotes
     * @param downvotes the number of downvotes
     * @param replies the list of replies
     */
    public ReadPostOutputData(long id, String title, String content, String username,
                              int upvotes, int downvotes, List<ReplyData> replies) {
        this(id, title, content, username, upvotes, downvotes, replies, null, new ArrayList<>());
    }

    /**
     * Constructs output data with referenced post but no referencing posts.
     *
     * @param id the post ID
     * @param title the post title
     * @param content the post content
     * @param username the author's username
     * @param upvotes the number of upvotes
     * @param downvotes the number of downvotes
     * @param replies the list of replies
     * @param referencedPost the referenced post data
     */
    public ReadPostOutputData(long id, String title, String content, String username,
                              int upvotes, int downvotes, List<ReplyData> replies,
                              ReferencedPostData referencedPost) {
        this(id, title, content, username, upvotes, downvotes, replies, referencedPost, new ArrayList<>());
    }

    /**
     * Constructs output data with full reference information.
     *
     * @param id the post ID
     * @param title the post title
     * @param content the post content
     * @param username the author's username
     * @param upvotes the number of upvotes
     * @param downvotes the number of downvotes
     * @param replies the list of replies
     * @param referencedPost the referenced post data
     * @param referencingPosts the list of posts that reference this post
     */
    public ReadPostOutputData(long id, String title, String content, String username,
                              int upvotes, int downvotes, List<ReplyData> replies,
                              ReferencedPostData referencedPost, List<ReferencingPostData> referencingPosts) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.replies = replies;
        this.referencedPost = referencedPost;
        this.referencingPosts = referencingPosts;
    }

    /**
     * Gets the post ID.
     *
     * @return the post ID
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the post title.
     *
     * @return the post title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the post content.
     *
     * @return the post content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the author's username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the number of upvotes.
     *
     * @return the upvote count
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Gets the number of downvotes.
     *
     * @return the downvote count
     */
    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Gets the list of replies.
     *
     * @return the replies
     */
    public List<ReplyData> getReplies() {
        return replies;
    }

    /**
     * Gets the referenced post data.
     *
     * @return the referenced post, or null if none
     */
    public ReferencedPostData getReferencedPost() {
        return referencedPost;
    }

    /**
     * Gets the list of posts that reference this post.
     *
     * @return the referencing posts
     */
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

        /**
         * Constructs a ReplyData.
         *
         * @param id the reply ID
         * @param username the author's username
         * @param content the reply content
         * @param upvotes the number of upvotes
         * @param downvotes the number of downvotes
         * @param nestedReplies the nested replies
         */
        public ReplyData(long id, String username, String content, int upvotes, int downvotes,
                        List<ReplyData> nestedReplies) {
            this.id = id;
            this.username = username;
            this.content = content;
            this.upvotes = upvotes;
            this.downvotes = downvotes;
            this.nestedReplies = nestedReplies;
        }

        /**
         * Gets the reply ID.
         *
         * @return the reply ID
         */
        public long getId() {
            return id;
        }

        /**
         * Gets the author's username.
         *
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * Gets the reply content.
         *
         * @return the content
         */
        public String getContent() {
            return content;
        }

        /**
         * Gets the number of upvotes.
         *
         * @return the upvote count
         */
        public int getUpvotes() {
            return upvotes;
        }

        /**
         * Gets the number of downvotes.
         *
         * @return the downvote count
         */
        public int getDownvotes() {
            return downvotes;
        }

        /**
         * Gets the nested replies.
         *
         * @return the nested replies
         */
        public List<ReplyData> getNestedReplies() {
            return nestedReplies;
        }

        /**
         * Sets the number of upvotes.
         *
         * @param upvotes the new upvote count
         */
        public void setUpvotes(int upvotes) {
            this.upvotes = upvotes;
        }

        /**
         * Sets the number of downvotes.
         *
         * @param downvotes the new downvote count
         */
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

        /**
         * Constructs a ReferencedPostData.
         *
         * @param id the post ID
         * @param title the post title
         * @param content the post content
         * @param username the author's username
         */
        public ReferencedPostData(long id, String title, String content, String username) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.username = username;
        }

        /**
         * Gets the post ID.
         *
         * @return the post ID
         */
        public long getId() {
            return id;
        }

        /**
         * Gets the post title.
         *
         * @return the post title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Gets the post content.
         *
         * @return the post content
         */
        public String getContent() {
            return content;
        }

        /**
         * Gets the author's username.
         *
         * @return the username
         */
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

        /**
         * Constructs a ReferencingPostData.
         *
         * @param id the post ID
         * @param title the post title
         * @param content the post content
         * @param username the author's username
         */
        public ReferencingPostData(long id, String title, String content, String username) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.username = username;
        }

        /**
         * Gets the post ID.
         *
         * @return the post ID
         */
        public long getId() {
            return id;
        }

        /**
         * Gets the post title.
         *
         * @return the post title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Gets the post content.
         *
         * @return the post content
         */
        public String getContent() {
            return content;
        }

        /**
         * Gets the author's username.
         *
         * @return the username
         */
        public String getUsername() {
            return username;
        }
    }
}
