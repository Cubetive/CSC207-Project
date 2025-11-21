package use_case.read_post;

import entities.OriginalPost;

/**
 * Data Access Interface for reading a specific post.
 */
public interface ReadPostDataAccessInterface {

    /**
     * Gets a post by its title.
     * @param title the title of the post to retrieve
     * @return the post with the given title, or null if not found
     */
    OriginalPost getPostByTitle(String title);

    /**
     * Gets a post by its unique ID.
     * @param id the unique identifier of the post to retrieve
     * @return the post with the given ID, or null if not found
     */
    OriginalPost getPostById(int id);
}
