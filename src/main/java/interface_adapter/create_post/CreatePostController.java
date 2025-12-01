package interface_adapter.create_post;

import use_case.create_post_use_case.CreatePostInputBoundary;
import use_case.create_post_use_case.CreatePostInputData;
import use_case.create_post_use_case.CreatePostInteractor;

public class CreatePostController {
    /**
     * Interactor for executing use case.
     */
    private final CreatePostInputBoundary createPostInteractor;
    /**
     * A measure of whether a post has been successfully created.
     */
    private boolean success = false;

    /**
     * A Constructor of controller with necessary elements.
     */
    public CreatePostController(CreatePostInputBoundary createPostInteractor) {
        this.createPostInteractor = createPostInteractor;
    }

    /**
     * Executes the Create Post Use Case.
     * @param title post title to create.
     * @param content the content to create.
     */
    public void execute(String title, String content) {
        execute(title, content, null);
    }

    /**
     * Executes the Create Post Use Case.
     * @param title post title to create.
     * @param content the content to create.
     * @param referencedPostId the id of the referenced post, if any.
     */
    public void execute(String title, String content, String referencedPostId) {
        final CreatePostInputData createPostInputData =
                new CreatePostInputData(title, content, referencedPostId);

        createPostInteractor.execute(createPostInputData);
        this.success = createPostInteractor.isSuccess();
    }

    /**
     * Tells interactor that we now need to abort the port
     * creation process.
     */
    public void switchToBrowseView() {
        createPostInteractor.switchToBrowseView();
    }

    /**
     * Reports if a post was successfully created.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Resets reporting measure for successful post creation.
     */
    public void resetSuccess() {
        this.success = false;
        this.createPostInteractor.resetSuccess();
    }

}
