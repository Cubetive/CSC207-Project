package use_case.browse_posts;

import java.util.List;

import entities.OriginalPost;

/**
 * Data Access Interface for browsing posts.
 */
public interface BrowsePostsDataAccessInterface {

    /**
     * Gets all posts from the data source.
     * @return a list of all posts
     */
    List<OriginalPost> getAllPosts();
}
