package use_case.reply_post;

import entities.Post;

public class ReplyPostInputData {
    private final String username;
    private final String content;
    private final Post parentPost;

    public ReplyPostInputData(String username, String content, Post parentPost) {
        this.username = username;
        this.content = content;
        this.parentPost = parentPost;
    }

    public String getUsername() { return this.username; }
    public String getContent() { return this.content; }
    public Post getParentPost() { return this.parentPost; }
}
