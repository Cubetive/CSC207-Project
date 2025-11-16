package use_case.create_post_use_case;

public interface CreatePostInputBoundary {

    void execute(CreatePostInputData createPostInputData);

    public void switchToBrowseView();

    public void switchToSearchView();

    public void switchToSignInView();

}
