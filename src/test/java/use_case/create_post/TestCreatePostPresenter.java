package use_case.create_post;

import interface_adapter.ViewManagerModel;
import interface_adapter.browse_posts.BrowsePostsViewModel;
import interface_adapter.create_post.CreatePostPresenter;
import interface_adapter.create_post.CreatePostViewModel;
import interface_adapter.read_post.ReadPostViewModel;
import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.create_post_use_case.CreatePostOutputData;
import view.PostReadingView;

public class TestCreatePostPresenter implements CreatePostOutputBoundary {

    public TestCreatePostPresenter() {}

    @Override
    public void prepareCreatedView(CreatePostOutputData createPostOutputData) {
    }

    public void prepareMissingFieldView(String error) {
    }

    public void switchToBrowseView() {}

    public void setPostReadingView(PostReadingView postReadingView) {}


}
