package use_case.create_post_use_case;

import entities.OriginalPost;

public class CreatePostInteractor implements CreatePostInputBoundary{
    private final CreatePostDataAccessInterface createPostUserDataAccessInterface;
    private final CreatePostOutputBoundary createPostOutputBoundary;
    //TODO Replace output boundary with presenter when implemented.
    //TODO Replace interface with data access object when implemented.

    public CreatePostInteractor(
            CreatePostDataAccessInterface createPostUserDataAccessInterface,
            CreatePostOutputBoundary createPostOutputBoundary) {
        this.createPostUserDataAccessInterface = createPostUserDataAccessInterface;
        this.createPostOutputBoundary = createPostOutputBoundary;
    }

    @Override
    public void execute(CreatePostInputData createPostInputData) {
        // Get the next original post id.
        long next_id = createPostUserDataAccessInterface;
        String content = createPostInputData.getContent();
        String title = createPostInputData.getTitle();
        String username = createPostInputData.getCreator_username();

        if (content.isEmpty() | title.isEmpty()) {
            createPostOutputBoundary.prepareMissingFieldView("Missing content or title.");
        }
        else {
            OriginalPost originalPost = new OriginalPost(title, content, username); // Create Post object.
            // TODO: Update for OriginalPost Signature

            createPostUserDataAccessInterface.save(originalPost); //saves the Post to Database.

            CreatePostOutputData createPostOutputData = new CreatePostOutputData(
                    originalPost.getTitle(),
                    originalPost.getContent(),
                    originalPost.getCreationDate(),
                    originalPost.getVotes()
            ); //Create the output object for display.
            createPostOutputBoundary.prepareCreatedView(createPostOutputData); //Send output to presenter.

        }
    }

    public void switchToBrowseView() {
        createPostOutputBoundary.switchToBrowseView();
    }

    public void switchToSearchView() {
        createPostOutputBoundary.switchToSearchView();
    }

    public void switchToSignUpView() {
        createPostOutputBoundary.switchToSignUpView();
    }
}
