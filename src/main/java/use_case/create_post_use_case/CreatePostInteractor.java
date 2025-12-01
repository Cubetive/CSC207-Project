package use_case.create_post_use_case;

import entities.OriginalPost;
import entities.Post;
import entities.User;
import use_case.reference_post.ReferencePostDataAccessInterface;
import use_case.session.SessionRepository;

public class CreatePostInteractor implements CreatePostInputBoundary{
    private final CreatePostDataAccessInterface filePostAccess;
    private final CreatePostOutputBoundary createPostPresenter;
    private boolean success;
    private final SessionRepository sessionRepository;

    public CreatePostInteractor(
            CreatePostDataAccessInterface filePostAccess,
            CreatePostOutputBoundary createPostPresenter,
            SessionRepository sessionRepository) {
        this.filePostAccess = filePostAccess;
        this.createPostPresenter = createPostPresenter;
        this.success = false;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void execute(CreatePostInputData createPostInputData) {
        final String content = createPostInputData.getContent();
        final String title = createPostInputData.getTitle();
        final String referencedPostId = createPostInputData.getReferencedPostId();
        final User currentUser = sessionRepository.getCurrentUser();
        final String username = currentUser != null ? currentUser.getUsername() : null;

        if (username == null) {
            createPostPresenter.prepareMissingFieldView("You must be logged in to create a post.");
            return;
        }

        if (content.isEmpty() || title.isEmpty()) {
            createPostPresenter.prepareMissingFieldView("Missing content or title.");
        } else {
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

    public void switchToBrowseView() {
        createPostPresenter.switchToBrowseView();
    }

    public boolean isSuccess() {
        return success;
    }

    public void resetSuccess() {
        this.success = false;
    }
}
