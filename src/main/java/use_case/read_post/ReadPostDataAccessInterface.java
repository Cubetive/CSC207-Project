package use_case.read_post;

import entities.OriginalPost;

/**
 * Data Access Interface for reading a specific post.
 */
public interface ReadPostDataAccessInterface {

    /**
     * Gets a post by its unique ID.
     * @param id the unique identifier of the post to retrieve
     * @return the post with the given ID, or null if not found
     */
    OriginalPost getPostById(int id);
}
