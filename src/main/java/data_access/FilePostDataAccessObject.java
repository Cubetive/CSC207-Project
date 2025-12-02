package data_access;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
import use_case.browse_posts.BrowsePostsDataAccessInterface;
import use_case.create_post_use_case.CreatePostDataAccessInterface;
import use_case.read_post.ReadPostDataAccessInterface;
import use_case.reply_post.ReplyPostDataAccessInterface;
import use_case.upvote_downvote.VoteDataAccessInterface;

/**
 * File-based implementation of the DAO for reading post data from JSON.
 */
public class FilePostDataAccessObject implements
        BrowsePostsDataAccessInterface,
        ReadPostDataAccessInterface,
        ReplyPostDataAccessInterface, 
        VoteDataAccessInterface,  
        CreatePostDataAccessInterface,
        use_case.reference_post.ReferencePostDataAccessInterface {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String USERNAME = "username";
    private static final String CONTENT = "content";
    private static final String DATE = "date";
    private static final String VOTES = "votes";
    private static final String REPLIES = "replies";
    private static final String REFERENCED_POST_ID = "referencedPostId";

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
        final List<OriginalPost> localPosts = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            final JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            if (jsonArray != null) {
                for (JsonElement element : jsonArray) {
                    final JsonObject postObj = element.getAsJsonObject();

                    final long id = postObj.get(ID).getAsLong();
                    final String title = postObj.get(TITLE).getAsString();
                    final String username = postObj.get(USERNAME).getAsString();
                    final String content = postObj.get(CONTENT).getAsString();
                    final Date creationDate = dateFormat.parse(postObj.get(DATE).getAsString());
                    int upvotes = 0;
                    int downvotes = 0;
                    if (postObj.has(VOTES) && !postObj.get(VOTES).isJsonNull()) {
                        final JsonArray votesArray = postObj.getAsJsonArray(VOTES);
                        if (votesArray.size() >= 2) {
                            upvotes = votesArray.get(0).getAsInt();
                            downvotes = votesArray.get(1).getAsInt();
                        }
                    }

                    final OriginalPost post = new OriginalPost(id, title, content, username,
                            creationDate, upvotes, downvotes);
                    postIdMap.put(id, post);

                    if (postObj.has(REPLIES)) {
                        final JsonArray repliesArray = postObj.getAsJsonArray(REPLIES);
                        parseReplies(repliesArray, post.getReplies(), dateFormat);
                    }

                    localPosts.add(post);
                }
                
                // Second pass: restore references after all posts are loaded
                for (int i = 0; i < jsonArray.size(); i++) {
                    final JsonElement element = jsonArray.get(i);
                    final JsonObject postObj = element.getAsJsonObject();
                    
                    if (postObj.has(REFERENCED_POST_ID) && !postObj.get(REFERENCED_POST_ID).isJsonNull()) {
                        final long referencedPostId = postObj.get(REFERENCED_POST_ID).getAsLong();
                        final Post referencedPost = postIdMap.get(referencedPostId);
                        if (referencedPost != null) {
                            localPosts.get(i).setReferencedPost(referencedPost);
                        }
                    }
                }
            }
        }
        catch (IOException | ParseException ex) {
            System.err.println("Error reading posts from file: " + ex.getMessage());
        }
        System.out.println("DAO DEBUG: getAllPosts loaded " + localPosts.size() + " posts from file.");

        this.posts = localPosts;
        return localPosts;
    }

    /**
     * Recursively parses reply posts from JSON and adds them to the given list.
     * @param repliesArray the JSON array containing reply data
     * @param repliesList the list to add the constructed ReplyPost entities to
     * @param simpleDateFormat the date format to use for parsing dates
     * @throws ParseException throws a parsing exception if parsing failed
     */
    private void parseReplies(JsonArray repliesArray, List<ReplyPost> repliesList, SimpleDateFormat simpleDateFormat)
            throws ParseException {
        for (JsonElement replyElement : repliesArray) {
            final JsonObject replyObj = replyElement.getAsJsonObject();

            final long id = replyObj.get(ID).getAsLong();
            final String username = replyObj.get(USERNAME).getAsString();
            final String content = replyObj.get(CONTENT).getAsString();
            final Date creationDate = simpleDateFormat.parse(replyObj.get(DATE).getAsString());

            int upvotes = 0;
            int downvotes = 0;
            if (replyObj.has(VOTES) && !replyObj.get(VOTES).isJsonNull()) {
                final JsonArray votesArray = replyObj.getAsJsonArray(VOTES);
                if (votesArray.size() >= 2) {
                    upvotes = votesArray.get(0).getAsInt();
                    downvotes = votesArray.get(1).getAsInt();
                }
            }

            final ReplyPost reply = new ReplyPost(id, username, content, creationDate, upvotes, downvotes);
            // Only add reply to map if an OriginalPost with this ID doesn't already exist
            // (OriginalPosts should take precedence since they're the main posts)
            final Post existingPost = postIdMap.get(id);
            if (existingPost == null || !(existingPost instanceof OriginalPost)) {
                postIdMap.put(id, reply);
            }

            // Recursively parse nested replies
            if (replyObj.has(REPLIES)) {
                final JsonArray nestedRepliesArray = replyObj.getAsJsonArray(REPLIES);
                parseReplies(nestedRepliesArray, reply.getReplies(), simpleDateFormat);
            }

            repliesList.add(reply);
        }
    }

    @Override
    public Post getPostById(long id) {
        if (!postIdMap.containsKey(id)) {
            // This repopulates postIdMap
            getAllPosts();
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
     * @param postsToSave the posts to save
     */
    private void save(List<OriginalPost> postsToSave) {
        final JsonArray jsonArray = new JsonArray();

        for (OriginalPost post : postsToSave) {
            final JsonObject postObj = new JsonObject();
            postObj.addProperty(ID, post.getId());
            postObj.addProperty(TITLE, post.getTitle());
            postObj.addProperty(USERNAME, post.getCreatorUsername());
            postObj.addProperty(DATE, dateFormat.format(post.getCreationDate()));
            postObj.addProperty(CONTENT, post.getContent());

            // Votes
            final JsonArray votesArray = new JsonArray();
            votesArray.add(post.getVotes()[0]);
            votesArray.add(post.getVotes()[1]);
            postObj.add(VOTES, votesArray);

            // Replies
            final JsonArray repliesArray = new JsonArray();
            for (ReplyPost reply : post.getReplies()) {
                // We use the helper formatReply which also does manual mapping
                final JsonObject replyObj = formatReply(reply);
                repliesArray.add(replyObj);
            }
            postObj.add(REPLIES, repliesArray);
            
            // Referenced post ID (if this post references another post)
            if (post.hasReference() && post.getReferencedPost() != null) {
                postObj.addProperty(REFERENCED_POST_ID, post.getReferencedPost().getId());
            }

            jsonArray.add(postObj);
        }

        try (Writer writer = new FileWriter(filePath)) {
            // Use gsonSaving for pretty printing
            gsonSaving.toJson(jsonArray, writer);
        }
        catch (IOException ex) {
            System.out.println("Error writing file: " + ex.getMessage());
        }
    }

    /**
     * Edit the post content.
     * @param id the id of the post
     * @param contentNew the new content
     */
    public void editPostContent(long id, String contentNew) {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            final List<Map<String, Object>> postings = mapper.readValue(
                    new File("posts.json"),
                    new TypeReference<List<Map<String, Object>>>() { }
            );

            for (Map<String, Object> post : postings) {
                if (((Number) post.get(ID)).longValue() == id) {
                    post.put(CONTENT, contentNew);
                }
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(
                    new File("posts.json"), postings
            );
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveVote(long id, int newUpvotes, int newDownvotes) {
        final List<OriginalPost> allPosts = getAllPosts();

        final boolean found = updateVoteInList(allPosts, id, newUpvotes, newDownvotes);

        if (found) {
            this.posts = allPosts;
            save(allPosts);
        }
    }

    // Helper to update the vote in the list we are about to save
    private boolean updateVoteInList(List<? extends Post> postList, long targetId, int upvote, int downvote) {
        for (Post post : postList) {
            if (post.getId() == targetId) {
                post.setUpvotes(upvote);
                post.setDownvotes(downvote);
                return true;
            }
            if (post instanceof OriginalPost) {
                if (updateVoteInList(((OriginalPost) post).getReplies(), targetId, upvote, downvote)) {
                    return true;
                }
            }
            else if (post instanceof ReplyPost) {
                if (updateVoteInList(((ReplyPost) post).getReplies(), targetId, upvote, downvote)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Format the reply into JsonObject.
     * @param replyPost the reply post object
     * @return the reply json object
     */
    public JsonObject formatReply(ReplyPost replyPost) {
        final JsonObject replyPostObj = new JsonObject();
        replyPostObj.addProperty(ID, replyPost.getId());
        replyPostObj.addProperty(USERNAME, replyPost.getCreatorUsername());
        replyPostObj.addProperty(DATE, dateFormat.format(replyPost.getCreationDate()));
        replyPostObj.addProperty(CONTENT, replyPost.getContent());
        // Votes
        final JsonArray votesArray = new JsonArray();
        votesArray.add(replyPost.getVotes()[0]);
        votesArray.add(replyPost.getVotes()[1]);
        replyPostObj.add(VOTES, votesArray);
        // Replies
        final JsonArray repliesArray = new JsonArray();
        for (ReplyPost reply : replyPost.getReplies()) {
            final JsonObject replyObj = formatReply(reply);
            repliesArray.add(replyObj);
        }
        replyPostObj.add(REPLIES, repliesArray);

        return replyPostObj;
    }

    // Save a reply to an original post.
    @Override
    public void save(ReplyPost replyPost, OriginalPost parentPost) {
        parentPost.addReply(replyPost);
        this.save();
    }

    // Save a reply to another reply.
    @Override
    public void save(ReplyPost replyPost, ReplyPost parentPost) {
        parentPost.addReply(replyPost);
        this.save();
    }

    // Save a new original post.
    @Override
    public void save(OriginalPost originalPost) {
        this.posts.add(originalPost);
        this.save();
    }
    
    // ReferencePostDataAccessInterface methods
    @Override
    public List<Post> searchPostsByKeyword(String keyword) {
        final List<Post> results = new ArrayList<>();
        final String lowerKeyword = keyword.toLowerCase();
        
        // Ensure posts are loaded
        getAllPosts();
        
        // Search through all posts (including replies)
        for (OriginalPost post : posts) {
            searchPostRecursive(post, lowerKeyword, results);
        }
        
        return results;
    }

    /**
     * Recursively searches through a post and its replies.
     * @param post The post to search for
     * @param lowerKeyword The keyword to search for
     * @param results The results
     */
    private void searchPostRecursive(Post post, String lowerKeyword, List<Post> results) {
        boolean matches = false;
        
        // Search in content
        if (post.getContent() != null && post.getContent().toLowerCase().contains(lowerKeyword)) {
            matches = true;
        }
        
        // Search in title for OriginalPost
        if (post instanceof OriginalPost) {
            final OriginalPost originalPost = (OriginalPost) post;
            if (originalPost.getTitle() != null && originalPost.getTitle().toLowerCase().contains(lowerKeyword)) {
                matches = true;
            }
        }
        
        // Search in creator username
        if (post.getCreatorUsername() != null && post.getCreatorUsername().toLowerCase().contains(lowerKeyword)) {
            matches = true;
        }
        
        if (matches) {
            results.add(post);
        }
        
        // Search in replies
        if (post instanceof OriginalPost) {
            for (ReplyPost reply : ((OriginalPost) post).getReplies()) {
                searchPostRecursive(reply, lowerKeyword, results);
            }
        }
        else if (post instanceof ReplyPost) {
            for (ReplyPost reply : ((ReplyPost) post).getReplies()) {
                searchPostRecursive(reply, lowerKeyword, results);
            }
        }
    }
    
    @Override
    public Post getPostById(String postId) {
        try {
            final long id = Long.parseLong(postId);
            return getPostById(id);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    
    @Override
    public void savePost(Post post) {
        if (post instanceof OriginalPost) {
            // Check if post already exists
            final OriginalPost originalPost = (OriginalPost) post;
            final List<OriginalPost> allPosts = getAllPosts();
            
            // Find and update existing post or add new one
            boolean found = false;
            for (int i = 0; i < allPosts.size(); i++) {
                if (allPosts.get(i).getId() == post.getId()) {
                    allPosts.set(i, originalPost);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                allPosts.add(originalPost);
            }
            
            this.posts = allPosts;
            this.save();
        }
        // For ReplyPost, we'd need to find the parent and update it
        // This is more complex and may require additional logic
    }
}
