package use_case.create_post_use_case;

import entities.OriginalPost;
import java.util.List;

public interface CreatePostDataAccessInterface {

    // Checks for existence of posts with the name indicated in their title.
    //Boolean existsWithName(String name);

    // Saves a post to database.
    /**
     * @param originalPost to be saved
     */
    void save(OriginalPost originalPost);

    /**
     * @return All posts.
     */
    List<OriginalPost> getAllPosts();

}
