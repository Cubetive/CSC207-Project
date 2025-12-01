package use_case.create_post_use_case;

public class CreatePostInputData {
    private final String title;
    private final String content;
    private final String referencedPostId;

    public CreatePostInputData(String title, String content) {
        this(title, content, null);
    }
    
    public CreatePostInputData(String title, String content, String referencedPostId) {
        this.title = title;
        this.content = content;
        this.referencedPostId = referencedPostId;
    }

    String getTitle() { return title; }

    String getContent() { return content; }
    
    String getReferencedPostId() { return referencedPostId; }
}