package use_case.browse_posts;

import java.util.Date;
import java.util.List;

/**
 * Output Data for the Browse Posts use case.
 */
public class BrowsePostsOutputData {

    private final List<PostData> posts;

    /**
     * Constructs the output data with the given list of posts.
     *
     * @param posts the list of post data
     */
    public BrowsePostsOutputData(List<PostData> posts) {
        this.posts = posts;
    }

    /**
     * Gets the list of posts.
     *
     * @return the list of post data
     */
    public List<PostData> getPosts() {
        return posts;
    }

    /**
     * Simple data holder for post information.
     */
    public static class PostData {
        private final long id;
        private final String title;
        private final String content;
        private final String username;
        private final Date creationDate;
        private final int upvotes;
        private final int downvotes;
        private final boolean hasReference;
        private final String referencedPostTitle;
        private final Long referencedPostId;

        /**
         * Constructs a PostData without reference information.
         *
         * @param id the post ID
         * @param title the post title
         * @param content the post content
         * @param username the author's username
         * @param creationDate the creation date
         * @param upvotes the number of upvotes
         * @param downvotes the number of downvotes
         */
        public PostData(long id, String title, String content, String username,
                       Date creationDate, int upvotes, int downvotes) {
            this(id, title, content, username, creationDate, upvotes, downvotes, false, null, null);
        }

        /**
         * Constructs a PostData with reference information.
         *
         * @param id the post ID
         * @param title the post title
         * @param content the post content
         * @param username the author's username
         * @param creationDate the creation date
         * @param upvotes the number of upvotes
         * @param downvotes the number of downvotes
         * @param hasReference whether the post has a reference
         * @param referencedPostTitle the title of the referenced post
         * @param referencedPostId the ID of the referenced post
         */
        public PostData(long id, String title, String content, String username,
                       Date creationDate, int upvotes, int downvotes,
                       boolean hasReference, String referencedPostTitle, Long referencedPostId) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.username = username;
            this.creationDate = creationDate;
            this.upvotes = upvotes;
            this.downvotes = downvotes;
            this.hasReference = hasReference;
            this.referencedPostTitle = referencedPostTitle;
            this.referencedPostId = referencedPostId;
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
         * Gets the creation date.
         *
         * @return the creation date
         */
        public Date getCreationDate() {
            return creationDate;
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
         * Checks if this post has a reference.
         *
         * @return true if has reference, false otherwise
         */
        public boolean hasReference() {
            return hasReference;
        }

        /**
         * Gets the title of the referenced post.
         *
         * @return the referenced post title, or null if no reference
         */
        public String getReferencedPostTitle() {
            return referencedPostTitle;
        }

        /**
         * Gets the ID of the referenced post.
         *
         * @return the referenced post ID, or null if no reference
         */
        public Long getReferencedPostId() {
            return referencedPostId;
        }
    }
}
