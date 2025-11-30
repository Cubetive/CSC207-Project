package use_case.browse_posts;

import java.util.Date;
import java.util.List;

/**
 * Output Data for the Browse Posts use case.
 */
public class BrowsePostsOutputData {

    private final List<PostData> posts;

    public BrowsePostsOutputData(List<PostData> posts) {
        this.posts = posts;
    }

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

        public PostData(long id, String title, String content, String username,
                       Date creationDate, int upvotes, int downvotes) {
            this(id, title, content, username, creationDate, upvotes, downvotes, false, null);
        }
        
        public PostData(long id, String title, String content, String username,
                       Date creationDate, int upvotes, int downvotes,
                       boolean hasReference, String referencedPostTitle) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.username = username;
            this.creationDate = creationDate;
            this.upvotes = upvotes;
            this.downvotes = downvotes;
            this.hasReference = hasReference;
            this.referencedPostTitle = referencedPostTitle;
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

        public Date getCreationDate() {
            return creationDate;
        }

        public int getUpvotes() {
            return upvotes;
        }

        public int getDownvotes() {
            return downvotes;
        }
        
        public boolean hasReference() {
            return hasReference;
        }
        
        public String getReferencedPostTitle() {
            return referencedPostTitle;
        }
    }
}
