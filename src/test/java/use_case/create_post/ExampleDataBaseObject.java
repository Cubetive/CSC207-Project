package use_case.create_post;

import data_access.FilePostDataAccessObject;
import entities.OriginalPost;

public class ExampleDataBaseObject extends FilePostDataAccessObject implements TestDatabaseObject {
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
}
