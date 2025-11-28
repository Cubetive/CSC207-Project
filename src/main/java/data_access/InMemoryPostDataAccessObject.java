package data_access;

import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
import use_case.reply_post.ReplyPostDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPostDataAccessObject implements ReplyPostDataAccessInterface {
    private final Map<Long, Post> postIdMap = new HashMap<>();

    // Dummy, waiting for implementation of create post DAI
    public void save(OriginalPost post) {
        postIdMap.put(post.getId(), post);
    }

    @Override
    public void save(ReplyPost replyPost, ReplyPost parentPost) {
        postIdMap.put(replyPost.getId(), replyPost);
        parentPost.addReply(replyPost);
    }

    @Override
    public void save(ReplyPost replyPost, OriginalPost parentPost) {
        postIdMap.put(replyPost.getId(), replyPost);
        parentPost.addReply(replyPost);
    }

    @Override
    public Post getPostById(long id) {
        return this.postIdMap.get(id);
    }
}
