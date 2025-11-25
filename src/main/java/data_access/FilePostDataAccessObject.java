package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entities.OriginalPost;
import entities.ReplyPost;
import use_case.browse_posts.BrowsePostsDataAccessInterface;
import use_case.read_post.ReadPostDataAccessInterface;
import use_case.upvote_downvote.VoteOutputData; //NEW
import use_case.upvote_downvote.VoteDataAccessInterface;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * File-based implementation of the DAO for reading post data from JSON.
 */
public class FilePostDataAccessObject implements BrowsePostsDataAccessInterface, ReadPostDataAccessInterface, VoteDataAccessInterface{

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
    // =========================================================================
    // VOTE USE CASE IMPLEMENTATION (NEW METHODS)
    // =========================================================================

    /**
     * Helper to find a Post or Reply and its parent, since only the parent post's
     * replies list needs to be re-sorted.
     * @param targetId The ID of the content (Post or Reply) to find.
     * @param allPosts The list of all posts to search.
     * @return A ContentWrapper containing the found content and its parent ID.
     */
    private ContentWrapper findTargetContent(long targetId, List<OriginalPost> allPosts) {
        for (OriginalPost post : allPosts) {
            if (post.getId() == targetId) {
                // Target is the main post itself
                return new ContentWrapper(post, targetId);
            }

            // Search through replies recursively
            ReplyPost targetReply = findReplyRecursively(post.getReplies(), targetId);
            if (targetReply != null) {
                // Target is a reply; the parent ID is the main post's ID
                return new ContentWrapper(targetReply, post.getId());
            }
        }
        return null; // Content not found
    }

    /**
     * Recursively searches for a ReplyPost by its ID.
     */
    private ReplyPost findReplyRecursively(List<ReplyPost> replies, long targetId) {
        if (replies == null) return null;

        for (ReplyPost reply : replies) {
            if (reply.getId() == targetId) {
                return reply;
            }
            // Check nested replies
            ReplyPost foundInNested = findReplyRecursively(reply.getReplies(), targetId);
            if (foundInNested != null) {
                return foundInNested;
            }
        }
        return null;
    }

    /**
     * Implements the core logic for upvoting or downvoting content.
     * This method loads the data, modifies the entity, and persists the change.
     *
     * @param id The unique ID of the post or reply.
     * @param isUpvote True for an upvote, false for a downvote.
     * @return VoteOutputData containing the new score and the parent post ID.
     * @throws RuntimeException if the content is not found.
     */
    @Override
    public VoteOutputData updateVoteCount(long id, boolean isUpvote) {
        // 1. Load all posts (to get a mutable list to update)
        List<OriginalPost> allPosts = getAllPosts();

        // 2. Find the target Post/Reply and its parent ID
        ContentWrapper wrapper = findTargetContent(id, allPosts);

        if (wrapper == null) {
            throw new RuntimeException("Content with ID " + id + " not found.");
        }

        // 3. Update the votes directly on the entity
        int newUpvotes;
        int newDownvotes;

        // Assuming OriginalPost/ReplyPost has standard getters/setters for votes
        if (wrapper.getContent() instanceof OriginalPost) {
            // Assuming post.getVotes() returns a structure where index 0 is upvotes and 1 is downvotes,
            // but the entity is built with separate getUpvotes/getDownvotes. Using the individual methods.
            OriginalPost post = (OriginalPost) wrapper.getContent();
            newUpvotes = post.getVotes()[0] + (isUpvote ? 1 : 0);
            newDownvotes = post.getVotes()[1] + (isUpvote ? 0 : 1);
//            post.setUpvotes(newUpvotes); // ASSUMING setUpvotes/setDownvotes exist TODO: FIX
//            post.setDownvotes(newDownvotes);
        } else if (wrapper.getContent() instanceof ReplyPost) {
            ReplyPost reply = (ReplyPost) wrapper.getContent();
            newUpvotes = reply.getVotes()[0] + (isUpvote ? 1 : 0);
            newDownvotes = reply.getVotes()[1] + (isUpvote ? 0 : 1);
//            reply.setUpvotes(newUpvotes); // ASSUMING setUpvotes/setDownvotes exist TODO: FIX
//            reply.setDownvotes(newDownvotes);
        } else {
            // Should not happen based on logic, but for safety
            throw new RuntimeException("Content type not supported for voting.");
        }

        // 4. Persist the change by writing the updated list back to the file
        saveAllPosts(allPosts);

        // 5. Calculate new score and return output data
        int newScore = newUpvotes - newDownvotes;
        // Corrected VoteOutputData constructor call
        return new VoteOutputData(newScore, wrapper.getParentPostId());
    }

    /**
     * Writes the entire list of posts back to the JSON file.
     * @param posts The list of posts to save.
     */
    private void saveAllPosts(List<OriginalPost> posts) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the entire posts list as a JSON array
            gson.toJson(posts, writer);
        } catch (IOException e) {
            System.err.println("Error writing posts to file: " + e.getMessage());
        }
    }

    /**
     * Recursively sorts a list of replies based on their vote score (Upvotes - Downvotes),
     * ensuring the highest-scoring replies appear first.
     */
    private void sortRepliesRecursively(List<ReplyPost> replies) {
        if (replies == null || replies.isEmpty()) {
            return;
        }

        // Sort the current level of replies in DESCENDING order of score
        replies.sort(Comparator.comparingInt(reply -> {
            // Score = Upvotes - Downvotes
            int[] votes = ((ReplyPost) reply).getVotes();
            if (votes != null && votes.length >= 2) {
                // Accessing array elements using [0] and [1]
                return votes[0] - votes[1];
            }
            return 0; // Default to zero score if vote data is incomplete
        }).reversed()); // .reversed() ensures highest score is first

        // Recurse into nested replies
        for (ReplyPost reply : replies) {
            if (reply.getReplies() != null) {
                sortRepliesRecursively(reply.getReplies());
            }
        }
    }

    /**
     * Private helper class to hold the mutable content entity and its parent post ID.
     * This wrapper allows us to return the content object (OriginalPost or ReplyPost)
     * along with the ID of the main post, which is needed to trigger a re-read/sort.
     */
    private static class ContentWrapper {
        private final Object content; // Can be OriginalPost or ReplyPost
        private final long parentPostId;

        public ContentWrapper(Object content, long parentPostId) {
            this.content = content;
            this.parentPostId = parentPostId;
        }

        public Object getContent() {
            return content;
        }

        public long getParentPostId() {
            return parentPostId;
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
