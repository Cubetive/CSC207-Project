package use_case.reference_post;

import java.util.List;

import entities.Post;

/**
 * Data Access Interface for the Reference Post Use Case.
 */
public interface ReferencePostDataAccessInterface {

    /**
     * Searches for posts by keyword in title or content.
     * @param keyword the keyword to search for
     * @return list of posts matching the keyword
     */
    List<Post> searchPostsByKeyword(String keyword);

    /**
     * Gets a post by its ID.
     * @param postId the ID of the post
     * @return the post if found, null otherwise
     */
    Post getPostById(String postId);

    /**
     * Saves a post with its reference.
     * @param post the post to save
     */
    void savePost(Post post);
}
