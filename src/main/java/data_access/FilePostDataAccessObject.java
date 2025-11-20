package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
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

                    if (postObj.has("replies")) {
                        final JsonArray repliesArray = postObj.getAsJsonArray("replies");
                        parseReplies(repliesArray, post.getReplies());
                    }

                    posts.add(post);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading posts from file: " + e.getMessage());
        }

        return posts;
    }

    /**
     * Recursively parses reply posts from JSON and adds them to the given list.
     * @param repliesArray the JSON array containing reply data
     * @param repliesList the list to add the constructed ReplyPost entities to
     */
    private void parseReplies(JsonArray repliesArray, List<Post> repliesList) {
        for (JsonElement replyElement : repliesArray) {
            final JsonObject replyObj = replyElement.getAsJsonObject();

            final String username = replyObj.get("username").getAsString();
            final String content = replyObj.get("content").getAsString();

            // Construct the ReplyPost entity through its constructor
            final ReplyPost reply = new ReplyPost(username, content);

            // Recursively parse nested replies (lowercase "replies")
            if (replyObj.has("replies")) {
                final JsonArray nestedRepliesArray = replyObj.getAsJsonArray("replies");
                parseReplies(nestedRepliesArray, reply.getReplies());
            }

            repliesList.add(reply);
        }
    }
}
