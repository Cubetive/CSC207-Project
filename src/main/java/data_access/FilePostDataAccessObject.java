package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entities.OriginalPost;
import use_case.browse_posts.BrowsePostsDataAccessInterface;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of the DAO for reading post data from JSON.
 */
public class FilePostDataAccessObject implements BrowsePostsDataAccessInterface {

    private final String filePath;
    private final Gson gson;

    /**
     * Creates a new FilePostDataAccessObject that reads from the given file.
     * @param filePath the path to the JSON file containing posts
     */
    public FilePostDataAccessObject(String filePath) {
        this.filePath = filePath;
        this.gson = new Gson();
    }

    @Override
    public List<OriginalPost> getAllPosts() {
        final List<OriginalPost> posts = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            final JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            if (jsonArray != null) {
                for (JsonElement element : jsonArray) {
                    final JsonObject postObj = element.getAsJsonObject();

                    final String title = postObj.get("title").getAsString();
                    final String username = postObj.get("username").getAsString();
                    final String content = postObj.get("content").getAsString();

                    final OriginalPost post = new OriginalPost(title, content, username);
                    posts.add(post);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading posts from file: " + e.getMessage());
        }

        return posts;
    }
}
