package use_case.create_post;

import java.util.List;

import entities.OriginalPost;
import use_case.create_post_use_case.CreatePostDataAccessInterface;

public class ExampleDatabaseObject2 implements
        TestDatabaseObject, CreatePostDataAccessInterface {
    /**
     * Creates a new FilePostDataAccessObject that reads from the given file.
     *
     * @param filePath the path to the JSON file containing posts
     */
    public ExampleDatabaseObject2(String filePath) {
    }

    public void save(OriginalPost post) {
    }

    @Override
    public List<OriginalPost> getAllPosts() {
        return List.of();
    }

    public OriginalPost save(OriginalPost post, String blurb) {
        return post;
    }
}
