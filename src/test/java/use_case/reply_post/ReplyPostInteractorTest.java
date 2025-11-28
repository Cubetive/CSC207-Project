package use_case.reply_post;

import data_access.InMemoryPostDataAccessObject;
import data_access.InMemorySessionRepository;
import entities.*;
import org.junit.jupiter.api.Test;
import use_case.session.SessionRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ReplyPostInteractorTest {
    void saveDummyOriginalPost(InMemoryPostDataAccessObject postRepository, OriginalPost originalPost) {
        postRepository.save(originalPost);
    }

    void saveDummyReplyPost(InMemoryPostDataAccessObject postRepository, ReplyPost replyPost, long parentId) {
        Post parentPost = postRepository.getPostById(parentId);
        if (parentPost instanceof OriginalPost) {
            postRepository.save(replyPost, (OriginalPost) parentPost);
        }
        else if (parentPost instanceof ReplyPost) {
            postRepository.save(replyPost, (ReplyPost) parentPost);
        }
    }

    @Test
    void successTestOriginalPost() {
        ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        CommonUserFactory commonUserFactory = new CommonUserFactory();
        User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Create Dummy Original Post as parent
        OriginalPost dummyPost = new OriginalPost("kevin_kaslana", "Dans la mémoire des 13 Chasseurs de flammes",
                "La fin des Chasseurs de flammes doit être grandiose et spectaculaire!");
        saveDummyOriginalPost((InMemoryPostDataAccessObject)postRepository, dummyPost);

        // Create input data
        ReplyPostInputData inputData = new ReplyPostInputData("Hi♪ Love Elf❤", dummyPost.getId());

        // Creates successful presenter that tests the success of this test case
        ReplyPostOutputBoundary successPresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                ReplyPost output = replyPostOutputData.getReplyPost();
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

        ReplyPostInputBoundary interactor = new ReplyPostInteractor(postRepository, successPresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void successTestReplyPost() {
        ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        CommonUserFactory commonUserFactory = new CommonUserFactory();
        User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Create Dummy Original Post
        OriginalPost dummyPost = new OriginalPost("kevin_kaslana", "Dans la mémoire des 13 Chasseurs de flammes",
                "La fin des Chasseurs de flammes doit être grandiose et spectaculaire!");
        saveDummyOriginalPost((InMemoryPostDataAccessObject)postRepository, dummyPost);

        // Create Dummy Reply Post as Parent
        ReplyPost dummyReply = new ReplyPost("yae_sakura", "Stop speaking fr*nch bro");
        saveDummyReplyPost((InMemoryPostDataAccessObject)postRepository, dummyReply, dummyPost.getId());

        // Create input data
        ReplyPostInputData inputData = new ReplyPostInputData("Hi♪ Love Elf❤", dummyReply.getId());

        // Creates successful presenter that tests the success of this test case
        ReplyPostOutputBoundary successPresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                ReplyPost output = replyPostOutputData.getReplyPost();
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

        ReplyPostInputBoundary interactor = new ReplyPostInteractor(postRepository, successPresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failureMissingFieldsTest() {
        ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        CommonUserFactory commonUserFactory = new CommonUserFactory();
        User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Create Dummy Original Post
        OriginalPost dummyPost = new OriginalPost("kevin_kaslana", "Dans la mémoire des 13 Chasseurs de flammes",
                "La fin des Chasseurs de flammes doit être grandiose et spectaculaire!");
        saveDummyOriginalPost((InMemoryPostDataAccessObject)postRepository, dummyPost);

        // Create input data
        ReplyPostInputData inputData = new ReplyPostInputData("", dummyPost.getId());

        // Creates failure presenter that tests the failure of this test case
        ReplyPostOutputBoundary failurePresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                assertEquals("Fill in missing fields.", errorMessage);
            }
        };

        ReplyPostInputBoundary interactor = new ReplyPostInteractor(postRepository, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failureCurrentUserNotFoundTest() {
        ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create Dummy Original Post
        OriginalPost dummyPost = new OriginalPost("kevin_kaslana", "Dans la mémoire des 13 Chasseurs de flammes",
                "La fin des Chasseurs de flammes doit être grandiose et spectaculaire!");
        saveDummyOriginalPost((InMemoryPostDataAccessObject)postRepository, dummyPost);

        // Create input data
        ReplyPostInputData inputData = new ReplyPostInputData("Hi♪ Love Elf❤", dummyPost.getId());

        // Creates failure presenter that tests the failure of this test case
        ReplyPostOutputBoundary failurePresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                assertEquals("Error: User is not logged in! Please try again.", errorMessage);
            }
        };

        ReplyPostInputBoundary interactor = new ReplyPostInteractor(postRepository, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }

    @Test
    void failureParentPostNotFoundTest() {
        ReplyPostInputData inputData = new ReplyPostInputData("Hi♪ Love Elf❤", -1);
        ReplyPostDataAccessInterface postRepository = new InMemoryPostDataAccessObject();
        SessionRepository sessionRepository = new InMemorySessionRepository();

        // Create "logged in" user for our post
        CommonUserFactory commonUserFactory = new CommonUserFactory();
        User user = commonUserFactory.create("Elysia", "elysia",
                "misspinkelf@gmail.com", "mlleelferose1111");
        sessionRepository.setCurrentUser(user);

        // Creates failure presenter that tests the failure of this test case
        ReplyPostOutputBoundary failurePresenter = new ReplyPostOutputBoundary() {
            @Override
            public void prepareSuccessView(ReplyPostOutputData replyPostOutputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                assertEquals("Error: Post not found!", errorMessage);
            }
        };

        ReplyPostInputBoundary interactor = new ReplyPostInteractor(postRepository, failurePresenter, sessionRepository);
        interactor.execute(inputData);
    }
}
