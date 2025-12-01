package use_case.create_post_use_case;

public interface CreatePostInputBoundary {

    /**
     * @param createPostInputData for post to be created and to be saved
     */
    void execute(CreatePostInputData createPostInputData);

    /**
     * Switch to the browse view to be saved.
     */
    void switchToBrowseView();

    /**
     * @return True iff post created.
     */
    boolean isSuccess();

    /**
     * Resets whether a success has been detected.
     */
    void resetSuccess();

}
