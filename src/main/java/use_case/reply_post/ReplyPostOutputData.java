package use_case.reply_post;

import entities.ReplyPost;

public class ReplyPostOutputData {
    private final ReplyPost replyPost;

    public ReplyPostOutputData(ReplyPost replyPost) {
        this.replyPost = replyPost;
    }

    public ReplyPost getReplyPost() {
        return this.replyPost;
    }
}
