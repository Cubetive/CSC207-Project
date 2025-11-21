package use_case.reply_post;

public class ReplyPostInputData {
    private final String username;
    private final String content;
    private final long parentId;

    public ReplyPostInputData(String username, String content, long parentId) {
        this.username = username;
        this.content = content;
        this.parentId = parentId;
    }

    public String getUsername() { return this.username; }
    public String getContent() { return this.content; }
    public long getParentId() { return this.parentId; }
}
