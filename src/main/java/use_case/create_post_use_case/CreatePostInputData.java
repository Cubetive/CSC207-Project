package use_case.create_post_use_case;

public class CreatePostInputData {
    private String title;
    private String content;
    private String creator_username = "";
    private String referencedPostId = null;

    public CreatePostInputData(String title, String content,  String creator_username) {
        this.title = title;
        this.content = content;
        this.creator_username = creator_username;
    }
    
    public CreatePostInputData(String title, String content, String creator_username, String referencedPostId) {
        this.title = title;
        this.content = content;
        this.creator_username = creator_username;
        this.referencedPostId = referencedPostId;
    }

    String getTitle() {return title;}

    String getContent() {return content;}

    String getCreator_username() {return creator_username;}
    
    String getReferencedPostId() {return referencedPostId;}

}