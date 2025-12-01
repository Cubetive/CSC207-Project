package data_access;

import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
import use_case.reply_post.ReplyPostDataAccessInterface;
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
public class InMemoryPostDataAccessObject implements ReplyPostDataAccessInterface,
                                                    ReferencePostDataAccessInterface {
    private final Map<Long, Post> postsById = new HashMap<>();

    /**
     * Adds a post to the data store.
     * @param postId the ID of the post
     * @param post the post to add
     */
    public void addPost(long postId, Post post) {
        postsById.put(postId, post);
    }

    @Override
    public void save(ReplyPost replyPost, ReplyPost parentPost) {
        addPost(replyPost.getId(), replyPost);
        parentPost.addReply(replyPost);
    }

    @Override
    public void save(ReplyPost replyPost, OriginalPost parentPost) {
        addPost(replyPost.getId(), replyPost);
        parentPost.addReply(replyPost);
    }

    @Override
    public Post getPostById(long id) {
        return this.postsById.get(id);
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
        try {
            final long id = Long.parseLong(postId);
            return postsById.get(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void savePost(Post post) {
        addPost(post.getId(), post);
    }

    /**
     * Gets all posts.
     * @return a map of all posts by ID
     */
    public Map<Long, Post> getAllPosts() {
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
