package data_access;

import com.google.gson.*;
import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
import use_case.browse_posts.BrowsePostsDataAccessInterface;
import use_case.create_post_use_case.CreatePostDataAccessInterface;
import use_case.read_post.ReadPostDataAccessInterface;
import use_case.reply_post.ReplyPostDataAccessInterface;
import use_case.upvote_downvote.VoteDataAccessInterface;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

/**
 * File-based implementation of the DAO for reading post data from JSON.
 */
public class FilePostDataAccessObject implements
        BrowsePostsDataAccessInterface,
        ReadPostDataAccessInterface,
        ReplyPostDataAccessInterface, VoteDataAccessInterface,  CreatePostDataAccessInterface{
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private final String filePath;
    private final Gson gson;
    private final Gson gsonSaving;
    private final Map<Long, Post> postIdMap = new HashMap<>();
    private List<OriginalPost> posts = new ArrayList<>();

    /**
     * Creates a new FilePostDataAccessObject that reads from the given file.
     * @param filePath the path to the JSON file containing posts
     */
    public FilePostDataAccessObject(String filePath) {
        this.filePath = filePath;
        this.gson = new Gson();
        this.gsonSaving = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<OriginalPost> getAllPosts() {
        final List<OriginalPost> posts = new ArrayList<>();

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
                    int upvotes = 0;
                    int downvotes = 0;
                    if (postObj.has("votes") && !postObj.get("votes").isJsonNull()) {
                        JsonArray votesArray = postObj.getAsJsonArray("votes");
                        if (votesArray.size() >= 2) {
                            upvotes = votesArray.get(0).getAsInt();
                            downvotes = votesArray.get(1).getAsInt();
                        }
                    }

                    final OriginalPost post = new OriginalPost(id, title, content, username, creationDate, upvotes, downvotes);
                    postIdMap.put(id, post);

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
        System.out.println("DAO DEBUG: getAllPosts loaded " + posts.size() + " posts from file.");

        this.posts = posts;
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

            int upvotes = 0;
            int downvotes = 0;
            if (replyObj.has("votes") && !replyObj.get("votes").isJsonNull()) {
                JsonArray votesArray = replyObj.getAsJsonArray("votes");
                if (votesArray.size() >= 2) {
                    upvotes = votesArray.get(0).getAsInt();
                    downvotes = votesArray.get(1).getAsInt();
                }
            }

            final ReplyPost reply = new ReplyPost(id, username, content, creationDate, upvotes, downvotes);
            // Only add reply to map if an OriginalPost with this ID doesn't already exist
            // (OriginalPosts should take precedence since they're the main posts)
            Post existingPost = postIdMap.get(id);
            if (existingPost == null || !(existingPost instanceof OriginalPost)) {
                postIdMap.put(id, reply);
            }

            // Recursively parse nested replies
            if (replyObj.has("replies")) {
                final JsonArray nestedRepliesArray = replyObj.getAsJsonArray("replies");
                parseReplies(nestedRepliesArray, reply.getReplies(), dateFormat);
            }

            repliesList.add(reply);
        }
    }

    @Override
    public Post getPostById(long id) {
        if (!postIdMap.containsKey(id)) {
            getAllPosts(); // This repopulates postIdMap
        }
        return postIdMap.get(id);
    }

    /**
     * Public save method that saves the current in-memory state.
     * Delegates to the unified logic below.
     */

    // Looks at post array stored in this object and saves its contents to given Gson path (JSON database)
    public void save() {
        save(this.posts);
    }

    /**
     * The Single Source of Truth for writing to the file.
     * Uses manual JSON construction to ensure keys match the reading logic.
     */
    private void save(List<OriginalPost> postsToSave) {
        JsonArray jsonArray = new JsonArray();

        for (OriginalPost post : postsToSave) {
            JsonObject postObj = new JsonObject();
            postObj.addProperty("id", post.getId());
            postObj.addProperty("title", post.getTitle());

            postObj.addProperty("username", post.getCreatorUsername());

            postObj.addProperty("date", dateFormat.format(post.getCreationDate()));

            postObj.addProperty("content", post.getContent());

            // Votes
            JsonArray votesArray = new JsonArray();
            votesArray.add(post.getVotes()[0]);
            votesArray.add(post.getVotes()[1]);
            postObj.add("votes", votesArray);

            // Replies
            JsonArray repliesArray = new JsonArray();
            for (ReplyPost reply : post.getReplies()) {
                // We use the helper formatReply which also does manual mapping
                JsonObject replyObj = formatReply(reply);
                repliesArray.add(replyObj);
            }
            postObj.add("replies", repliesArray);

            jsonArray.add(postObj);
        }

        try (Writer writer = new FileWriter(filePath)) {
            // Use gsonSaving for pretty printing
            gsonSaving.toJson(jsonArray, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + e.getMessage());
        }
    }

    public void editPostContent(long id, String contentNew) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Map<String, Object>> postings = mapper.readValue(
                    new File("posts.json"),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            for (Map<String, Object> post : postings) {
                if (((Number) post.get("id")).longValue() == id) {
                    post.put("content", contentNew);
                }
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(
                    new File("posts.json"), postings
            );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveVote(long id, int newUpvotes, int newDownvotes) {
        List<OriginalPost> allPosts = getAllPosts();

        boolean found = updateVoteInList(allPosts, id, newUpvotes, newDownvotes);

        if (found) {
            this.posts = allPosts;
            save(allPosts);
        }
    }

    // Helper to update the vote in the list we are about to save
    private boolean updateVoteInList(List<? extends Post> posts, long targetId, int up, int down) {
        for (Post post : posts) {
            if (post.getId() == targetId) {
                post.setUpvotes(up);
                post.setDownvotes(down);
                return true;
            }
            if (post instanceof OriginalPost) {
                if (updateVoteInList(((OriginalPost) post).getReplies(), targetId, up, down)) return true;
            } else if (post instanceof ReplyPost) {
                if (updateVoteInList(((ReplyPost) post).getReplies(), targetId, up, down)) return true;
            }
        }
        return false;
    }

    public JsonObject formatReply(ReplyPost replyPost) {
        JsonObject replyPostObj = new JsonObject();
        replyPostObj.addProperty("id", replyPost.getId());
        replyPostObj.addProperty("username", replyPost.getCreatorUsername());
        replyPostObj.addProperty("date", dateFormat.format(replyPost.getCreationDate()));
        replyPostObj.addProperty("content", replyPost.getContent());
        // Votes
        JsonArray votesArray = new JsonArray();
        votesArray.add(replyPost.getVotes()[0]);
        votesArray.add(replyPost.getVotes()[1]);
        replyPostObj.add("votes", votesArray);
        // Replies
        JsonArray repliesArray = new JsonArray();
        for (ReplyPost reply : replyPost.getReplies()) {
            JsonObject replyObj = formatReply(reply);
            repliesArray.add(replyObj);
        }
        replyPostObj.add("replies", repliesArray);

        return replyPostObj;
    }
    //Save a reply to an original post.
    @Override
    public void save(ReplyPost replyPost, OriginalPost parentPost) {
        parentPost.addReply(replyPost);
        this.save();
    }

    //Save a reply to another reply.
    @Override
    public void save(ReplyPost replyPost, ReplyPost parentPost) {
        parentPost.addReply(replyPost);
        this.save();
    }

    //Save a new original post.
    @Override
    public void save(OriginalPost originalPost) {
        List<OriginalPost> currentPosts = this.getAllPosts();
        this.posts.add(originalPost);
        this.save();
    }
}
