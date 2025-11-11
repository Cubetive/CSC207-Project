package use_case.create_post_use_case;

public interface CreatePostUserDataAccessInterface {

    // Checks for existence of posts with the name indicated in their title.
    Boolean existswithname(String name);

    // Saves a post to database.
    void save(Post post);

    // Fetches a specific post.
    Post getPost(String title);

    //Get all viewable Posts.
    Post<> getAllPosts();

    //Get all Posts with keyword in title
    Post<> getSelectPosts(String keyword);
}
