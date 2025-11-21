package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entities.OriginalPost;
import entities.ReplyPost;
import use_case.browse_posts.BrowsePostsDataAccessInterface;
import use_case.read_post.ReadPostDataAccessInterface;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * File-based implementation of the DAO for reading post data from JSON.
 */
public class FilePostDataAccessObject implements BrowsePostsDataAccessInterface, ReadPostDataAccessInterface {

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
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try (FileReader reader = new FileReader(filePath)) {
            final JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            if (jsonArray != null) {
                for (JsonElement element : jsonArray) {
                    final JsonObject postObj = element.getAsJsonObject();

                    final long id = postObj.get("id").getAsLong();
                    final String title = postObj.get("title").getAsString();
                    final String username = postObj.get("username").getAsString();
                    final String content = postObj.get("content").getAsString();
                    final Date creationDate = dateFormat.parse(postObj.get("date").getAsString());
                    final JsonArray votesArray = postObj.getAsJsonArray("votes");
                    final int upvotes = votesArray.get(0).getAsInt();
                    final int downvotes = votesArray.get(1).getAsInt();

                    final OriginalPost post = new OriginalPost(id, title, content, username, creationDate, upvotes, downvotes);

                    if (postObj.has("replies")) {
                        final JsonArray repliesArray = postObj.getAsJsonArray("replies");
                        parseReplies(repliesArray, post.getReplies(), dateFormat);
                    }

                    posts.add(post);
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error reading posts from file: " + e.getMessage());
        }

        return posts;
    }

    /**
     * Recursively parses reply posts from JSON and adds them to the given list.
     * @param repliesArray the JSON array containing reply data
     * @param repliesList the list to add the constructed ReplyPost entities to
     * @param dateFormat the date format to use for parsing dates
     */
    private void parseReplies(JsonArray repliesArray, List<ReplyPost> repliesList, SimpleDateFormat dateFormat) throws ParseException {

        for (JsonElement replyElement : repliesArray) {
            final JsonObject replyObj = replyElement.getAsJsonObject();

            final long id = replyObj.get("id").getAsLong();
            final String username = replyObj.get("username").getAsString();
            final String content = replyObj.get("content").getAsString();
            final Date creationDate = dateFormat.parse(replyObj.get("date").getAsString());
            final JsonArray votesArray = replyObj.getAsJsonArray("votes");
            final int upvotes = votesArray.get(0).getAsInt();
            final int downvotes = votesArray.get(1).getAsInt();

            final ReplyPost reply = new ReplyPost(id, username, content, creationDate, upvotes, downvotes);

            // Recursively parse nested replies
            if (replyObj.has("replies")) {
                final JsonArray nestedRepliesArray = replyObj.getAsJsonArray("replies");
                parseReplies(nestedRepliesArray, reply.getReplies(), dateFormat);
            }

            repliesList.add(reply);
        }
    }

    @Override
    public OriginalPost getPostById(long id) {
        final List<OriginalPost> allPosts = getAllPosts();

        for (OriginalPost post : allPosts) {
            if (post.getId() == id) {
                return post;
            }
        }

        return null;
    }
}
