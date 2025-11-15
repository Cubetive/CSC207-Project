package use_case.create_post_use_case;

public interface CreatePostOutputBoundary {

    void prepareCreatedView(CreatePostOutputData createPostOutputData);

    void prepareMissingFieldView();

    void switchToBrowseView();

    void switchToSearchView();
}
