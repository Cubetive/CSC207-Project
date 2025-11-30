package use_case.create_post_use_case;

import view.PostReadingView;

public interface CreatePostOutputBoundary {

    void prepareCreatedView(CreatePostOutputData createPostOutputData);

    void prepareMissingFieldView(String error);

    void switchToBrowseView();

    void setPostReadingView(PostReadingView postReadingView);
}
