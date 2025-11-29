package use_case.reply_post;

public class ReplyPostInputData {
    private final String content;
    private final long parentId;

    public ReplyPostInputData(String content, long parentId) {
        this.content = content;
        this.parentId = parentId;
    }

    public String getContent() { return this.content; }
    public long getParentId() { return this.parentId; }
}
