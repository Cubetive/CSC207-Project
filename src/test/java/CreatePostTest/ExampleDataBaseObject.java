package CreatePostTest;

import entities.OriginalPost;
import entities.Post;
import use_case.create_post_use_case.CreatePostInputBoundary;
import use_case.create_post_use_case.CreatePostUserDataAccessInterface;

import java.util.ArrayList;

public class ExampleDataBaseObject implements CreatePostUserDataAccessInterface {
    public void save(Post post) {}

    public Post getPost(String title) {
        if (title.equals("RandomTitle")) {
            return new OriginalPost("RandomTitle", "RandomContent", "SomeUsername");
        }
        else {
            return new OriginalPost("notRandomTitle", "notRandomContent", "notSomeUsername");
        }
    }

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new OriginalPost("RandomTitle", "RandomContent", "SomeUsername"));
        return posts;
    }

    public ArrayList<Post> getSelectPosts(String keyword) {
        return getAllPosts();
    }
}
