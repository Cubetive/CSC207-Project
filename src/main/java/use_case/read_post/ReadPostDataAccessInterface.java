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
}
