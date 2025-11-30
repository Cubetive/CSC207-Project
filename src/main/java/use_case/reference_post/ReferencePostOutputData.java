package use_case.reference_post;

import java.util.List;

/**
 * Output Data for the Reference Post Use Case.
 */
public class ReferencePostOutputData {
    private final List<PostSearchResult> searchResults;
    private final PostSearchResult referencedPost;
    private final String currentPostId;
    private final boolean useCaseFailed;

    /**
     * Constructor for search results.
     * @param searchResults list of posts matching the search
     * @param currentPostId the ID of the current post
     */
    public ReferencePostOutputData(List<PostSearchResult> searchResults, String currentPostId) {
        this.searchResults = searchResults;
        this.referencedPost = null;
        this.currentPostId = currentPostId;
        this.useCaseFailed = false;
    }

    /**
     * Constructor for successful reference.
     * @param referencedPost the post that was referenced
     * @param currentPostId the ID of the current post
     * @param useCaseFailed whether the use case failed
     */
    public ReferencePostOutputData(PostSearchResult referencedPost, String currentPostId, boolean useCaseFailed) {
        this.searchResults = null;
        this.referencedPost = referencedPost;
        this.currentPostId = currentPostId;
        this.useCaseFailed = useCaseFailed;
    }

    public List<PostSearchResult> getSearchResults() {
        return searchResults;
    }

    public PostSearchResult getReferencedPost() {
        return referencedPost;
    }

    public String getCurrentPostId() {
        return currentPostId;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    /**
     * Inner class representing a post in search results.
     */
    public static class PostSearchResult {
        private final String postId;
        private final String title;
        private final String content;
        private final String creatorUsername;
        private final String creationDate;

        public PostSearchResult(String postId, String title, String content, 
                              String creatorUsername, String creationDate) {
            this.postId = postId;
            this.title = title;
            this.content = content;
            this.creatorUsername = creatorUsername;
            this.creationDate = creationDate;
        }

        public String getPostId() {
            return postId;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getCreatorUsername() {
            return creatorUsername;
        }

        public String getCreationDate() {
            return creationDate;
        }
    }
}
