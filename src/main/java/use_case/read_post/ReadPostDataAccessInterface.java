package use_case.read_post;

import entities.OriginalPost;
import entities.Post;

/**
 * Data Access Interface for reading a specific post.
 */
import java.util.List;

public interface ReadPostDataAccessInterface {

    /**
     * Gets a post by its unique ID.
     * @param id the unique identifier of the post to retrieve
     * @return the post with the given ID, or null if not found
     */
    Post getPostById(long id);
    
    /**
     * Gets all posts. Used to find posts that reference a specific post.
     * @return list of all original posts
     */
    List<OriginalPost> getAllPosts();
}
