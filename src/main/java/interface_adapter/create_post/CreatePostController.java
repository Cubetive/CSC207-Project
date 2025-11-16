package interface_adapter.create_post;

import use_case.create_post_use_case.CreatePostInputBoundary;
import use_case.create_post_use_case.CreatePostInputData;

public class CreatePostController {
    final CreatePostInputBoundary createPostInteractor;

    public CreatePostController(CreatePostInputBoundary createPostInteractor) {
        this.createPostInteractor = createPostInteractor;
    }

    /*
     * Executes the Create Post Use Case.
     * @param username the username to sign up
     * @param password1 the password
     * @param password2 the password repeated
     */
    public void execute(String title, String content,  String creator_username) {
        final CreatePostInputData createPostInputData = new CreatePostInputData(title, content, creator_username);

        createPostInteractor.execute(createPostInputData);
    }

    public void switchToBrowseView() {
        createPostInteractor.switchToBrowseView();
    }

    public void switchToSearchView() {
        createPostInteractor.switchToSearchView();
    }

    public void switchToSignInView() {
        createPostInteractor.switchToSignInView();
    }
}
