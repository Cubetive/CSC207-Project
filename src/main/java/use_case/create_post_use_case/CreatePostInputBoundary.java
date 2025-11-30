package use_case.create_post_use_case;

public interface CreatePostInputBoundary {

    void execute(CreatePostInputData createPostInputData);

    void switchToBrowseView();

    void switchToSearchView();

    boolean isSuccess();

    void resetSuccess();

}
