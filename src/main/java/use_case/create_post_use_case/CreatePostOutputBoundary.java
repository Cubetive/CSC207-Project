package use_case.create_post_use_case;

public interface CreatePostOutputBoundary {

    void prepareCreatedView(CreatePostOutputData createPostOutputData);

    void prepareMissingFieldView(String error);

    void switchToBrowseView();

    void switchToSearchView();

    void switchToSignUpView();
}
