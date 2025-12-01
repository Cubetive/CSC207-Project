package use_case.create_post_use_case;

import view.PostReadingView;

public interface CreatePostOutputBoundary {

    /**
     * Tell presenter a post was successfully created and should now
     * be displayed.
     * @param createPostOutputData data about post that was just created.
     */
    void prepareCreatedView(CreatePostOutputData createPostOutputData);

    /**
     * A post was not successfully created, display an error.
     * @param error error to be displayed.
     */
    void prepareMissingFieldView(String error);

    /**
     * Tell presenter to switch to browsing view
     * (post creation process cancelled.
     */
    void switchToBrowseView();

    /**
     * Set up the post reading view for display after
     * post is created.
     * @param postReadingView post reading view to be used.
     */
    void setPostReadingView(PostReadingView postReadingView);
}
