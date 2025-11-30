package CreatePostTest;

import data_access.FilePostDataAccessObject;
import entities.OriginalPost;
import entities.Post;
import use_case.create_post_use_case.CreatePostDataAccessInterface;

import java.util.ArrayList;
import java.util.Date;

public class ExampleDataBaseObject extends FilePostDataAccessObject {
    /**
     * Creates a new FilePostDataAccessObject that reads from the given file.
     *
     * @param filePath the path to the JSON file containing posts
     */
    public ExampleDataBaseObject(String filePath) {
        super(filePath);
    }

    public void save(OriginalPost post) {
        //Overwrite for test case.
        super.save(post);
    }
}
