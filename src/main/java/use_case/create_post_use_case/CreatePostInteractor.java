package use_case.create_post_use_case;

import entities.OriginalPost;
import use_case.session.SessionRepository;

import java.util.Date;
import java.util.List;

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
        // TODO: Get the next original post id.
        String content = createPostInputData.getContent();
        String title = createPostInputData.getTitle();
        String username = sessionRepository.getCurrentUser().getUsername();

        if (content.isEmpty() || title.isEmpty()) {
            createPostPresenter.prepareMissingFieldView("Missing content or title.");
        }
        else {
            List<OriginalPost> list = filePostAccess.getAllPosts();
            OriginalPost originalPost = new OriginalPost(username, title, content); // Create Post object.

            filePostAccess.save(originalPost); //saves the Post to Database.

            CreatePostOutputData createPostOutputData = new CreatePostOutputData(originalPost);
            //Create the output object for display.
            createPostPresenter.prepareCreatedView(createPostOutputData); //Send output to presenter.
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
