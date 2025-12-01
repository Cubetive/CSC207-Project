package use_case.read_post;

import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ReadPostInteractorTest {

    private TestReadPostDataAccess dataAccess;
    private TestReadPostOutputBoundary outputBoundary;
    private ReadPostInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestReadPostDataAccess();
        outputBoundary = new TestReadPostOutputBoundary();
        interactor = new ReadPostInteractor(dataAccess, outputBoundary);
    }

    @Test
    void executeSuccessWithPost() {
        // Arrange
        Date date = new Date();
        OriginalPost post = new OriginalPost(1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        dataAccess.addPost(1L, post);

        ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertFalse(outputBoundary.failCalled);
        assertNotNull(outputBoundary.outputData);

        // Verify InputData getter (covers ReadPostInputData)
        assertEquals(1L, inputData.getPostId());

        // Verify OutputData getters (covers ReadPostOutputData)
        assertEquals(1L, outputBoundary.outputData.getId());
        assertEquals("Test Title", outputBoundary.outputData.getTitle());
        assertEquals("Test Content", outputBoundary.outputData.getContent());
        assertEquals("testUser", outputBoundary.outputData.getUsername());
        assertEquals(5, outputBoundary.outputData.getUpvotes());
        assertEquals(2, outputBoundary.outputData.getDownvotes());
        assertNotNull(outputBoundary.outputData.getReplies());
    }

    @Test
    void executeSuccessWithReplies() {
        // Arrange
        Date date = new Date();
        OriginalPost post = new OriginalPost(1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        ReplyPost reply1 = new ReplyPost(2L, "replyUser1", "Reply 1", date, 3, 1);
        ReplyPost reply2 = new ReplyPost(3L, "replyUser2", "Reply 2", date, 1, 0);
        post.addReply(reply1);
        post.addReply(reply2);
        dataAccess.addPost(1L, post);

        ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertEquals(2, outputBoundary.outputData.getReplies().size());

        // Verify ReplyData getters (covers ReadPostOutputData.ReplyData)
        ReadPostOutputData.ReplyData firstReply = outputBoundary.outputData.getReplies().get(0);
        assertEquals(2L, firstReply.getId());
        assertEquals("replyUser1", firstReply.getUsername());
        assertEquals("Reply 1", firstReply.getContent());
        assertEquals(3, firstReply.getUpvotes());
        assertEquals(1, firstReply.getDownvotes());
        assertNotNull(firstReply.getNestedReplies());
    }

    @Test
    void executeSuccessWithNestedReplies() {
        // Arrange
        Date date = new Date();
        OriginalPost post = new OriginalPost(1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        ReplyPost reply1 = new ReplyPost(2L, "replyUser1", "Reply 1", date, 3, 1);
        ReplyPost nestedReply = new ReplyPost(3L, "nestedUser", "Nested Reply", date, 1, 0);
        reply1.addReply(nestedReply);
        post.addReply(reply1);
        dataAccess.addPost(1L, post);

        ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertEquals(1, outputBoundary.outputData.getReplies().size());
        assertEquals(1, outputBoundary.outputData.getReplies().get(0).getNestedReplies().size());

        ReadPostOutputData.ReplyData nestedReplyData = outputBoundary.outputData.getReplies().get(0).getNestedReplies().get(0);
        assertEquals(3L, nestedReplyData.getId());
        assertEquals("nestedUser", nestedReplyData.getUsername());
        assertEquals("Nested Reply", nestedReplyData.getContent());
    }

    @Test
    void executePostNotFound() {
        // Arrange
        ReadPostInputData inputData = new ReadPostInputData(999L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertEquals("Post not found with ID: 999", outputBoundary.errorMessage);
    }

    @Test
    void executeFailureWhenExceptionThrown() {
        // Arrange
        dataAccess.shouldThrowException = true;
        ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertTrue(outputBoundary.errorMessage.contains("Failed to load post:"));
    }

    @Test
    void testReplyDataSetters() {
        // Test the setUpvotes and setDownvotes methods on ReplyData
        ReadPostOutputData.ReplyData replyData = new ReadPostOutputData.ReplyData(
                1L, "user", "content", 0, 0, new ArrayList<>()
        );

        replyData.setUpvotes(10);
        replyData.setDownvotes(5);

        assertEquals(10, replyData.getUpvotes());
        assertEquals(5, replyData.getDownvotes());
    }

    // Test helper classes

    private static class TestReadPostDataAccess implements ReadPostDataAccessInterface {
        private final Map<Long, Post> posts = new HashMap<>();
        boolean shouldThrowException = false;

        void addPost(Long id, Post post) {
            posts.put(id, post);
        }

        @Override
        public Post getPostById(long id) {
            if (shouldThrowException) {
                throw new RuntimeException("Database error");
            }
            return posts.get(id);
        }

        public List<OriginalPost> getAllPosts() {
            return null;
        }
    }

    private static class TestReadPostOutputBoundary implements ReadPostOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        ReadPostOutputData outputData;
        String errorMessage;

        @Override
        public void prepareSuccessView(ReadPostOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failCalled = true;
            this.errorMessage = errorMessage;
        }
    }
}
