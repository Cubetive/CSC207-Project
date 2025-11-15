package use_case.create_post_use_case;

public class CreatePostInputData {
    private String title;
    private String content;
    private String creator_username;

    public CreatePostInputData(String title, String content,  String creator_username) {
        this.title = title;
        this.content = content;
    }

    String getTitle() {return title;}

    String getContent() {return content;}

    String getCreator_username() {return creator_username;}

}
