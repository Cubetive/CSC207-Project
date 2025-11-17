package data_access;

import entities.OriginalPost;
import entities.Post;
import use_case.reference_post.ReferencePostDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing post data.
 * This implementation uses a HashMap to store posts by ID.
 * Implements the ReferencePostDataAccessInterface for the reference post use case.
 */
public class InMemoryPostDataAccessObject implements ReferencePostDataAccessInterface {

    private final Map<String, Post> postsById = new HashMap<>();

    /**
     * Adds a post to the data store.
     * @param postId the ID of the post
     * @param post the post to add
     */
    public void addPost(String postId, Post post) {
        postsById.put(postId, post);
    }

    @Override
    public List<Post> searchPostsByKeyword(String keyword) {
        final List<Post> results = new ArrayList<>();
        final String lowerKeyword = keyword.toLowerCase();

        for (Post post : postsById.values()) {
            boolean matches = false;

            // Search in content
            if (post.getContent() != null && 
                post.getContent().toLowerCase().contains(lowerKeyword)) {
                matches = true;
            }

            // Search in title for OriginalPost
            if (post instanceof OriginalPost) {
                final OriginalPost originalPost = (OriginalPost) post;
                if (originalPost.getTitle() != null && 
                    originalPost.getTitle().toLowerCase().contains(lowerKeyword)) {
                    matches = true;
                }
            }

            // Search in creator username
            if (post.getCreatorUsername() != null && 
                post.getCreatorUsername().toLowerCase().contains(lowerKeyword)) {
                matches = true;
            }

            if (matches) {
                results.add(post);
            }
        }

        return results;
    }

    @Override
    public Post getPostById(String postId) {
        return postsById.get(postId);
    }

    @Override
    public void savePost(Post post) {
        // For in-memory implementation, we need to find the post by reference
        // and update it. Since we're modifying the post in place, 
        // we just need to ensure it's in our map.
        for (Map.Entry<String, Post> entry : postsById.entrySet()) {
            if (entry.getValue() == post) {
                postsById.put(entry.getKey(), post);
                return;
            }
        }
    }

    /**
     * Gets all posts.
     * @return a map of all posts by ID
     */
    public Map<String, Post> getAllPosts() {
        return new HashMap<>(postsById);
    }

    /**
     * Clears all posts from storage.
     * Useful for testing.
     */
    public void clear() {
        postsById.clear();
    }
}
