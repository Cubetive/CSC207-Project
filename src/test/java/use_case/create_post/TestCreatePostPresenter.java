package use_case.create_post;

import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.create_post_use_case.CreatePostOutputData;
import view.PostReadingView;

public class TestCreatePostPresenter implements CreatePostOutputBoundary {

    public TestCreatePostPresenter() {

    }

    @Override
    public void prepareCreatedView(CreatePostOutputData createPostOutputData) {
    }

    public void prepareMissingFieldView(String error) {
    }

    public void switchToBrowseView() {

    }

    public void setPostReadingView(PostReadingView postReadingView) {
    }
}
