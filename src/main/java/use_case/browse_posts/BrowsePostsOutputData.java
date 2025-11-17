package use_case.browse_posts;

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
        private final String title;
        private final String content;
        private final String username;

        public PostData(String title, String content, String username) {
            this.title = title;
            this.content = content;
            this.username = username;
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
