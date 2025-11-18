package use_case.reply_post;

import entities.ReplyPost;

/**
 * The DAO interface for the Reply Thread use case.
 */
public interface ReplyPostDataAccessInterface {
    /**
     * Saves the reply of a post.
     * @param replyPost the reply to save
     */
    void save(ReplyPost replyPost);
}
