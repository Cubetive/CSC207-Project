package use_case.create_post;

import use_case.create_post_use_case.CreatePostOutputBoundary;
import use_case.create_post_use_case.CreatePostOutputData;
import view.PostReadingView;

public class TestCreatePostPresenter implements CreatePostOutputBoundary {

    /**
     * Whether we are now browsing.
     */
    private String state;

    public TestCreatePostPresenter() {

    }

    @Override
    public void prepareCreatedView(CreatePostOutputData createPostOutputData) {
    }

    public void prepareMissingFieldView(String error) {
        this.state = "failed";
    }

    public void switchToBrowseView() {
        this.state = "browse";
    }

    public void setPostReadingView(PostReadingView postReadingView) {
    }

    /**
     * Check for current state.
     * @return True iff browsing now.
     */
    public String isState() {
        return this.state;
    }
}
