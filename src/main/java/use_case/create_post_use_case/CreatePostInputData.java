package use_case.create_post_use_case;

public class CreatePostInputData {
    private String title;
    private String content;

    public CreatePostInputData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    String getTitle() {return title;}

    String getContent() {return content;}

}
