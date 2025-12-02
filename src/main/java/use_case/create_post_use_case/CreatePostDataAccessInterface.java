package use_case.create_post_use_case;

import java.util.List;

import entities.OriginalPost;

public interface CreatePostDataAccessInterface {

    // Checks for existence of posts with the name indicated in their title.
    // Boolean existsWithName(String name);

    // Saves a post to database.
    /**
     * Saves the post.
     * @param originalPost to be saved.
     */
    void save(OriginalPost originalPost);

    /**
     * Gets all posts currently existing.
     * @return All posts.
     */
    List<OriginalPost> getAllPosts();

}
