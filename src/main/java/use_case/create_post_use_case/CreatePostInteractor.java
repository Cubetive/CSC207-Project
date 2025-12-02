package use_case.create_post_use_case;

import entities.OriginalPost;
import entities.Post;
import entities.User;
import use_case.reference_post.ReferencePostDataAccessInterface;
import use_case.session.SessionRepository;

public class CreatePostInteractor implements CreatePostInputBoundary {
    /**
     * Database where originalPost is to be saved.
     */
    private final CreatePostDataAccessInterface filePostAccess;
    /**
     * Presenter to display created post.
     */
    private final CreatePostOutputBoundary createPostPresenter;
    /**
     * Measure of whether or not an object has been created.
     */
    private boolean success;

    /**
     * Session repository of user information.
     */
    private final SessionRepository sessionRepository;

    /**
     * CreatePostInteractor constructor.
     * @param filePostAccess database access object to save the new post to.
     * @param createPostPresenter presenter to display results.
     * @param sessionRepository chich stores information about user currently
     *                          using app.
     */
    public CreatePostInteractor(
            CreatePostDataAccessInterface filePostAccess,
            CreatePostOutputBoundary createPostPresenter,
            SessionRepository sessionRepository) {
        this.filePostAccess = filePostAccess;
        this.createPostPresenter = createPostPresenter;
        this.success = false;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Execute the use case: attempt to create and save post,
     * then tell Presenter to display it.
     * If a field was missing, or the session repository does not have a user
     * (improperly constructed) then display an error.
     */
    @Override
    public void execute(CreatePostInputData createPostInputData) {
        final String content = createPostInputData.getContent();
        final String title = createPostInputData.getTitle();
        final String referencedPostId =
                createPostInputData.getReferencedPostId();
        final User currentUser =
                sessionRepository.getCurrentUser();
        final String username =
                currentUser != null ? currentUser.getUsername() : null;

        if (username == null) {
            createPostPresenter.prepareMissingFieldView("You must be logged in to create a post.");
            return;
        }

        if (content.isEmpty() || title.isEmpty()) {
            createPostPresenter.prepareMissingFieldView("Missing content or title.");
        }
        else {
            final OriginalPost originalPost = new OriginalPost(username, title, content);

            // Attach referenced post if provided
            if (referencedPostId != null && !referencedPostId.trim().isEmpty()) {
                if (filePostAccess instanceof ReferencePostDataAccessInterface) {
                    final ReferencePostDataAccessInterface referenceAccess =
                            (ReferencePostDataAccessInterface) filePostAccess;
                    final Post referencedPost = referenceAccess.getPostById(referencedPostId);
                    if (referencedPost != null) {
                        originalPost.setReferencedPost(referencedPost);
                    }
                }
            }

            filePostAccess.save(originalPost);

            final CreatePostOutputData createPostOutputData = new CreatePostOutputData(originalPost);
            createPostPresenter.prepareCreatedView(createPostOutputData);
            this.success = true;
        }
    }

    /**
     * Tell presenter to switch to the browse view
     * i.e. abort creation of post.
     */
    public void switchToBrowseView() {
        createPostPresenter.switchToBrowseView();
    }

    /**
     * Report if a post was just created.
     * @return True iff a post was created.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Reset measure of success.
     */
    public void resetSuccess() {
        this.success = false;
    }
}
