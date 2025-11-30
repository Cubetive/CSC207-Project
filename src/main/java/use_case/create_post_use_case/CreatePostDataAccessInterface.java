package use_case.create_post_use_case;

import entities.OriginalPost;
import entities.Post;

import java.util.ArrayList;
import java.util.List;

public interface CreatePostDataAccessInterface {

    // Checks for existence of posts with the name indicated in their title.
    //Boolean existsWithName(String name);

    // Saves a post to database.
    void save(OriginalPost originalPost);

    List<OriginalPost> getAllPosts();

}
