package use_case.upvote_downvote;

import entities.OriginalPost;
import entities.Post;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class VoteInteractorTest {

    @Test
    void successUpvoteTest() {
        // 1. Arrange: Create input data (Upvote post #123)
        VoteInputData inputData = new VoteInputData(true, 123L);

        // Create a fake DAO that returns a specific post
        VoteDataAccessInterface successDAO = new VoteDataAccessInterface() {
            @Override
            public Post getPostById(long id) {
                // Return a dummy post with 0 upvotes
                return new OriginalPost(123L, "Test Title", "Content", "User", new Date(), 0, 0);
            }

            @Override
            public void saveVote(long contentId, int newUpvotes, int newDownvotes) {
                // Verify the DAO is told to save the correct new numbers
                assertEquals(123L, contentId);
                assertEquals(1, newUpvotes); // Should increment from 0 to 1
                assertEquals(0, newDownvotes);
            }
        };

        // Create a fake Presenter to capture the success output
        VoteOutputBoundary successPresenter = new VoteOutputBoundary() {
            @Override
            public void prepareSuccessView(VoteOutputData outputData) {
                // Verify the output data sent to the presenter
                assertEquals(123L, outputData.getId());
                assertEquals(1, outputData.getNewUpvotes());
                assertEquals(0, outputData.getNewDownvotes());
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        // 2. Act: Execute the Interactor
        VoteInteractor interactor = new VoteInteractor(successDAO, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void successDownvoteTest() {
        // 1. Arrange: Create input data (Downvote post #123)
        VoteInputData inputData = new VoteInputData(false, 123L);

        // Fake DAO
        VoteDataAccessInterface successDAO = new VoteDataAccessInterface() {
            @Override
            public Post getPostById(long id) {
                // Return a dummy post with 10 upvotes and 5 downvotes
                return new OriginalPost(123L, "Test Title", "Content", "User", new Date(), 10, 5);
            }

            @Override
            public void saveVote(long contentId, int newUpvotes, int newDownvotes) {
                // Verify logic
                assertEquals(123L, contentId);
                assertEquals(10, newUpvotes); // Should stay same
                assertEquals(6, newDownvotes); // Should increment from 5 to 6
            }
        };

        // Fake Presenter
        VoteOutputBoundary successPresenter = new VoteOutputBoundary() {
            @Override
            public void prepareSuccessView(VoteOutputData outputData) {
                assertEquals(6, outputData.getNewDownvotes());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        // 2. Act
        VoteInteractor interactor = new VoteInteractor(successDAO, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failurePostNotFoundTest() {
        // 1. Arrange: Input data for a non-existent ID
        VoteInputData inputData = new VoteInputData(true, 999L);

        // Fake DAO that returns null
        VoteDataAccessInterface failureDAO = new VoteDataAccessInterface() {
            @Override
            public Post getPostById(long id) {
                return null; // Simulate post not found
            }

            @Override
            public void saveVote(long contentId, int newUpvotes, int newDownvotes) {
                fail("DAO save should not be called if post is missing.");
            }
        };

        // Fake Presenter that expects failure
        VoteOutputBoundary failurePresenter = new VoteOutputBoundary() {
            @Override
            public void prepareSuccessView(VoteOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                // Verify the error message
                assertEquals("Content with ID 999 not found.", error);
            }
        };

        // 2. Act
        VoteInteractor interactor = new VoteInteractor(failureDAO, failurePresenter);
        interactor.execute(inputData);
    }
}