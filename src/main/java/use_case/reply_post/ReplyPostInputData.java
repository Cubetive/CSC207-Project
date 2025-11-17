package use_case.reply_post;

public class ReplyPostInputData {
    private final String username;
    private final String content;

    public ReplyPostInputData(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getUsername() { return this.username; }
    public String getContent() { return this.content; }
}
