package use_case.reply_post;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import data_access.InMemoryPostDataAccessObject;
import data_access.InMemorySessionRepository;
import entities.*;
import use_case.session.SessionRepository;

public class ReplyPostInteractorTest {
    void saveDummyOriginalPost(InMemoryPostDataAccessObject postRepository, OriginalPost originalPost) {
        postRepository.savePost(originalPost);
    }

    void saveDummyReplyPost(InMemoryPostDataAccessObject postRepository, ReplyPost replyPost, long parentId) {
        final Post parentPost = postRepository.getPostById(parentId);
        if (parentPost instanceof OriginalPost) {
            postRepository.save(replyPost, (OriginalPost) parentPost);
        }
        else if (parentPost instanceof ReplyPost) {
            postRepository.save(replyPost, (ReplyPost) parentPost);
        }
    }

    @Test
    void successTestOriginalPost() {
        final ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        final SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        final CommonUserFactory commonUserFactory = new CommonUserFactory();
        final User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Create Dummy Original Post as parent
        final OriginalPost dummyPost = new OriginalPost("kevin_kaslana", "Dans la mémoire des 13 Chasseurs de flammes",
                "La fin des Chasseurs de flammes doit être grandiose et spectaculaire!");
        saveDummyOriginalPost((InMemoryPostDataAccessObject) postRepository, dummyPost);

        // Create input data
        final ReplyPostInputData inputData = new ReplyPostInputData("Hi♪ Love Elf❤", dummyPost.getId());

        // Creates successful presenter that tests the success of this test case
        final ReplyPostOutputBoundary successPresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                final ReplyPost output = replyPostOutputData.getReplyPost();
                // Checking the content is correct
                assertEquals("elysia", output.getCreatorUsername());
                assertEquals("Hi♪ Love Elf❤", output.getContent());
                // Checking if the reply is properly saved
                assertTrue(dummyPost.getReplies().contains(output));
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                fail("Use case failure is unexpected.");
            }
        };

        final ReplyPostInputBoundary interactor =
                new ReplyPostInteractor(postRepository, successPresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void successTestReplyPost() {
        final ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        final SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        final CommonUserFactory commonUserFactory = new CommonUserFactory();
        final User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Create Dummy Original Post
        final OriginalPost dummyPost = new OriginalPost("kevin_kaslana", "Dans la mémoire des 13 Chasseurs de flammes",
                "La fin des Chasseurs de flammes doit être grandiose et spectaculaire!");
        saveDummyOriginalPost((InMemoryPostDataAccessObject) postRepository, dummyPost);

        // Create Dummy Reply Post as Parent
        final ReplyPost dummyReply = new ReplyPost("yae_sakura", "Stop speaking fr*nch bro");
        saveDummyReplyPost((InMemoryPostDataAccessObject) postRepository, dummyReply, dummyPost.getId());

        // Create input data
        final ReplyPostInputData inputData = new ReplyPostInputData("Hi♪ Love Elf❤", dummyReply.getId());

        // Creates successful presenter that tests the success of this test case
        final ReplyPostOutputBoundary successPresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                final ReplyPost output = replyPostOutputData.getReplyPost();
                // Checking the content is correct
                assertEquals("elysia", output.getCreatorUsername());
                assertEquals("Hi♪ Love Elf❤", output.getContent());
                // Checking if the reply is properly saved
                assertTrue(dummyReply.getReplies().contains(output));
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                fail("Use case failure is unexpected.");
            }
        };

        final ReplyPostInputBoundary interactor =
                new ReplyPostInteractor(postRepository, successPresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failureMissingFieldsTest() {
        final ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        final SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        final CommonUserFactory commonUserFactory = new CommonUserFactory();
        final User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Create Dummy Original Post
        final OriginalPost dummyPost = new OriginalPost("kevin_kaslana", "Dans la mémoire des 13 Chasseurs de flammes",
                "La fin des Chasseurs de flammes doit être grandiose et spectaculaire!");
        saveDummyOriginalPost((InMemoryPostDataAccessObject) postRepository, dummyPost);

        // Create input data
        final ReplyPostInputData inputData = new ReplyPostInputData("", dummyPost.getId());

        // Creates failure presenter that tests the failure of this test case
        final ReplyPostOutputBoundary failurePresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                assertEquals("Fill in missing fields.", errorMessage);
            }
        };

        final ReplyPostInputBoundary interactor =
                new ReplyPostInteractor(postRepository, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failureCurrentUserNotFoundTest() {
        final ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        final SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create Dummy Original Post
        final OriginalPost dummyPost = new OriginalPost("kevin_kaslana", "Dans la mémoire des 13 Chasseurs de flammes",
                "La fin des Chasseurs de flammes doit être grandiose et spectaculaire!");
        saveDummyOriginalPost((InMemoryPostDataAccessObject) postRepository, dummyPost);

        // Create input data
        final ReplyPostInputData inputData = new ReplyPostInputData("Hi♪ Love Elf❤", dummyPost.getId());

        // Creates failure presenter that tests the failure of this test case
        final ReplyPostOutputBoundary failurePresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                assertEquals("Error: User is not logged in! Please try again.", errorMessage);
            }
        };

        final ReplyPostInputBoundary interactor =
                new ReplyPostInteractor(postRepository, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failureParentPostNotFoundTest() {
        final ReplyPostInputData inputData = new ReplyPostInputData("Hi♪ Love Elf❤", -1);
        final ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        final SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        final CommonUserFactory commonUserFactory = new CommonUserFactory();
        final User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Creates failure presenter that tests the failure of this test case
        final ReplyPostOutputBoundary failurePresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                assertEquals("Error: Post not found!", errorMessage);
            }
        };

        final ReplyPostInputBoundary interactor =
                new ReplyPostInteractor(postRepository, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }
}
