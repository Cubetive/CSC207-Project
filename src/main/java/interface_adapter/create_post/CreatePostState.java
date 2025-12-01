package interface_adapter.create_post;

/**
 * The state for the Create Post View Model.
 */
public class CreatePostState {
    private String title = "";
    private String content = "";
    private String missingError = "";

    public CreatePostState(CreatePostState failedState) {
        this.title = failedState.title;
        this.content = failedState.content;
        this.missingError = failedState.missingError;
    }

    public CreatePostState() {}

    //Getters

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getMissingError() {
        return missingError;
    }

    //Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMissingError(String missingError) {
        this.missingError = missingError;
    }

}
