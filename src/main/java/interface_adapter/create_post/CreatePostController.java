package interface_adapter.create_post;

import use_case.create_post_use_case.CreatePostInputBoundary;
import use_case.create_post_use_case.CreatePostInputData;
import use_case.create_post_use_case.CreatePostInteractor;

public class CreatePostController {
    final CreatePostInputBoundary createPostInteractor;
    private boolean success = false;

    public CreatePostController(CreatePostInputBoundary createPostInteractor) {
        this.createPostInteractor = createPostInteractor;
    }

    /*
     * Executes the Create Post Use Case.
     * @param username the username to sign up
     * @param password1 the password
     * @param password2 the password repeated
     */
    public void execute(String title, String content) {
        // TODO: need to obtain current user from inMemorySessionRepository
        final CreatePostInputData createPostInputData = new CreatePostInputData(title, content);

        createPostInteractor.execute(createPostInputData);
        this.success = createPostInteractor.isSuccess();
    }

    public void switchToBrowseView() {
        createPostInteractor.switchToBrowseView();
    }

    public void switchToSearchView() {
        createPostInteractor.switchToSearchView();
    }

    public boolean isSuccess() {
        return success;
    }

    public void resetSuccess() {
        this.success = false;
        this.createPostInteractor.resetSuccess();
    }

}
