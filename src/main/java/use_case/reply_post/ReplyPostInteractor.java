package use_case.reply_post;

import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
import entities.User;
import use_case.session.SessionRepository;

public class ReplyPostInteractor implements ReplyPostInputBoundary {
    private final ReplyPostDataAccessInterface replyPostDataAccessObject;
    private final ReplyPostOutputBoundary replyPostPresenter;
    private final SessionRepository sessionRepository;

    public ReplyPostInteractor(ReplyPostDataAccessInterface replyPostDataAccessObject,
                               ReplyPostOutputBoundary replyPostPresenter,
                               SessionRepository sessionRepository) {
        this.replyPostDataAccessObject = replyPostDataAccessObject;
        this.replyPostPresenter = replyPostPresenter;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void execute(ReplyPostInputData replyPostInputData) {
        // Get the user object from current session
        final User user = sessionRepository.getCurrentUser();
        if (user == null) {
            replyPostPresenter.prepareFailureView("Error: User is not logged in! Please try again.");
            return;
        }

        // Get the username from the current logged-in user
        final String username = user.getUsername();
        final String content = replyPostInputData.getContent();
        final long parentId = replyPostInputData.getParentId();

        // Treat null, empty, or whitespace-only content as "missing"
        if (content == null || content.trim().isEmpty()) {
            replyPostPresenter.prepareFailureView("Fill in missing fields.");
        }
        else {
            final ReplyPost replyPost = new ReplyPost(username, content);
            final Post parentPost = replyPostDataAccessObject.getPostById(parentId);
            // Casting to either Original Posts and Reply Posts for saving purposes
            if (parentPost instanceof OriginalPost) {
                replyPostDataAccessObject.save(replyPost, (OriginalPost) parentPost);
            }
            else if (parentPost instanceof ReplyPost) {
                replyPostDataAccessObject.save(replyPost, (ReplyPost) parentPost);
            }
            else if (parentPost == null) {
                // Parent post doesn't exist (should not be the case for most of the time, but just as a precaution)
                replyPostPresenter.prepareFailureView("Error: Post not found!");
                return;
            }

            final ReplyPostOutputData replyPostOutputData = new ReplyPostOutputData(replyPost);
            replyPostPresenter.prepareSuccessView(replyPostOutputData);
        }
    }
}
