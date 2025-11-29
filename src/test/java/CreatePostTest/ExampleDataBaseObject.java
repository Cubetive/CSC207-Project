package CreatePostTest;

import data_access.FilePostDataAccessObject;
import entities.OriginalPost;
import entities.Post;
import use_case.create_post_use_case.CreatePostDataAccessInterface;

import java.util.ArrayList;
import java.util.Date;

public class ExampleDataBaseObject extends FilePostDataAccessObject {
    public void save(OriginalPost post) {}
}
