package use_case.create_post;

import java.util.Date;

import data_access.FilePostDataAccessObject;
import entities.OriginalPost;
import entities.Post;
import use_case.reference_post.ReferencePostDataAccessInterface;

public class ExampleDataBaseObject extends FilePostDataAccessObject implements
        TestDatabaseObject, ReferencePostDataAccessInterface {
    /**
     * Creates a new FilePostDataAccessObject that reads from the given file.
     *
     * @param filePath the path to the JSON file containing posts
     */
    public ExampleDataBaseObject(String filePath) {
        super(filePath);
    }

    public void save(OriginalPost post) {
        super.save(post);
    }

    public OriginalPost save(OriginalPost post, String blurb) {
        return post;
    }

    @Override
    public Post getPostById(long id) {
        if (id == 0) {
            return null;
        }
        return new OriginalPost(999, "t", "c", "u", new Date(), 0, 0);
    }
}
