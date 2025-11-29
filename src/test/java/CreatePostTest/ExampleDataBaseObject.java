package CreatePostTest;

import entities.OriginalPost;
import entities.Post;
import use_case.create_post_use_case.CreatePostDataAccessInterface;

import java.util.ArrayList;
import java.util.Date;

public class ExampleDataBaseObject implements CreatePostDataAccessInterface {
    public void save(OriginalPost post) {}
}
