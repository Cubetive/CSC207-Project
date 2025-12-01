package use_case.reply_post;

import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;

/**
 * The DAO interface for the Reply Thread use case.
 */
public interface ReplyPostDataAccessInterface {
    /**
     * Saves the reply of a post.
     * @param replyPost the reply to save
     * @param parentPost the parent of the reply post
     */
    void save(ReplyPost replyPost, OriginalPost parentPost);

    /**
     * Saves the reply of a post.
     * @param replyPost the reply to save
     * @param parentPost the parent of the reply post
     */
    void save(ReplyPost replyPost, ReplyPost parentPost);

    /**
     * Get a post through id.
     * @param id post id
     * @return the post by id
     */
    Post getPostById(long id);
}
