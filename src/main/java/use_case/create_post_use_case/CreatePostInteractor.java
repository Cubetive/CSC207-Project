package use_case.create_post_use_case;

import entities.OriginalPost;
import entities.Post;
import use_case.reference_post.ReferencePostDataAccessInterface;

import java.util.Date;

public class CreatePostInteractor implements CreatePostInputBoundary{
    private final CreatePostDataAccessInterface filePostAccess;
    private final CreatePostOutputBoundary createPostPresenter;
    //TODO Replace interface with data access object when implemented.

    public CreatePostInteractor(
            CreatePostDataAccessInterface filePostAccess,
            CreatePostOutputBoundary createPostPresenter) {
        this.filePostAccess = filePostAccess;
        this.createPostPresenter = createPostPresenter;
    }

    @Override
    public void execute(CreatePostInputData createPostInputData) {
        // TODO: Get the next original post id.
        long next_id = 0;
        String content = createPostInputData.getContent();
        String title = createPostInputData.getTitle();
        String username = createPostInputData.getCreator_username();
        String referencedPostId = createPostInputData.getReferencedPostId();

        if (content.isEmpty() || title.isEmpty()) {
            createPostPresenter.prepareMissingFieldView("Missing content or title.");
        }
        else {
            OriginalPost originalPost = new OriginalPost(next_id, title, content, username,
                    new Date(), 0, 0); // Create Post object.

            // Attach referenced post if provided
            if (referencedPostId != null && !referencedPostId.trim().isEmpty()) {
                // Check if filePostAccess also implements ReferencePostDataAccessInterface
                if (filePostAccess instanceof ReferencePostDataAccessInterface) {
                    final ReferencePostDataAccessInterface referenceAccess = 
                            (ReferencePostDataAccessInterface) filePostAccess;
                    final Post referencedPost = referenceAccess.getPostById(referencedPostId);
                    if (referencedPost != null) {
                        originalPost.setReferencedPost(referencedPost);
                    }
                }
            }

            filePostAccess.save(originalPost); //saves the Post to Database.

            CreatePostOutputData createPostOutputData = new CreatePostOutputData(originalPost);
            //Create the output object for display.
            createPostPresenter.prepareCreatedView(createPostOutputData); //Send output to presenter.

        }
    }

    public void switchToBrowseView() {
        createPostPresenter.switchToBrowseView();
    }

    public void switchToSearchView() {
        createPostPresenter.switchToSearchView();
    }

    public void switchToSignUpView() {
        createPostPresenter.switchToSignUpView();
    }
}
