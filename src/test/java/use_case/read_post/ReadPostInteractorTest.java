package use_case.read_post;

import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    void executeSuccessWithReferencedOriginalPost() {
        // Arrange - create a post that references another original post
        Date date = new Date();
        OriginalPost referencedPost = new OriginalPost(1L, "Referenced Title", "Referenced Content", "refUser", date, 3, 1);
        OriginalPost post = new OriginalPost(2L, "Test Title", "Test Content", "testUser", date, 5, 2);
        post.setReferencedPost(referencedPost);
        dataAccess.addPost(1L, referencedPost);
        dataAccess.addPost(2L, post);

        ReadPostInputData inputData = new ReadPostInputData(2L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertNotNull(outputBoundary.outputData.getReferencedPost());
        assertEquals(1L, outputBoundary.outputData.getReferencedPost().getId());
        assertEquals("Referenced Title", outputBoundary.outputData.getReferencedPost().getTitle());
        assertEquals("Referenced Content", outputBoundary.outputData.getReferencedPost().getContent());
        assertEquals("refUser", outputBoundary.outputData.getReferencedPost().getUsername());
    }

    @Test
    void executeSuccessWithReferencedReplyPost() {
        // Arrange - create a post that references a reply post
        Date date = new Date();
        ReplyPost referencedReply = new ReplyPost(10L, "replyUser", "Reply Content", date, 1, 0);
        OriginalPost post = new OriginalPost(2L, "Test Title", "Test Content", "testUser", date, 5, 2);
        post.setReferencedPost(referencedReply);
        dataAccess.addPost(2L, post);

        ReadPostInputData inputData = new ReadPostInputData(2L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertNotNull(outputBoundary.outputData.getReferencedPost());
        assertEquals(10L, outputBoundary.outputData.getReferencedPost().getId());
        // For reply posts, title should be empty string
        assertEquals("", outputBoundary.outputData.getReferencedPost().getTitle());
        assertEquals("Reply Content", outputBoundary.outputData.getReferencedPost().getContent());
    }

    @Test
    void executeSuccessWithReferencingPosts() {
        // Arrange - create a post and another post that references it
        Date date = new Date();
        OriginalPost mainPost = new OriginalPost(1L, "Main Title", "Main Content", "mainUser", date, 5, 2);
        OriginalPost referencingPost = new OriginalPost(2L, "Referencing Title", "Referencing Content", "refUser", date, 3, 1);
        referencingPost.setReferencedPost(mainPost);
        dataAccess.addPost(1L, mainPost);
        dataAccess.addPost(2L, referencingPost);

        ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertNotNull(outputBoundary.outputData.getReferencingPosts());
        assertEquals(1, outputBoundary.outputData.getReferencingPosts().size());

        ReadPostOutputData.ReferencingPostData refData = outputBoundary.outputData.getReferencingPosts().get(0);
        assertEquals(2L, refData.getId());
        assertEquals("Referencing Title", refData.getTitle());
        assertEquals("Referencing Content", refData.getContent());
        assertEquals("refUser", refData.getUsername());
    }

    @Test
    void executeSuccessNoReferencingPosts() {
        // Arrange - create a post with no references to it
        Date date = new Date();
        OriginalPost post = new OriginalPost(1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        dataAccess.addPost(1L, post);

        ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertNotNull(outputBoundary.outputData.getReferencingPosts());
        assertTrue(outputBoundary.outputData.getReferencingPosts().isEmpty());
    }

    @Test
    void executeWhenGetAllPostsThrowsException() {
        // Arrange - create a data access that throws on getAllPosts
        TestReadPostDataAccess failingDataAccess = new TestReadPostDataAccess();
        failingDataAccess.shouldThrowOnGetAllPosts = true;
        ReadPostInteractor testInteractor = new ReadPostInteractor(failingDataAccess, outputBoundary);

        Date date = new Date();
        OriginalPost post = new OriginalPost(1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        failingDataAccess.addPost(1L, post);

        ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        testInteractor.execute(inputData);

        // Assert - should still succeed, just without referencing posts
        assertTrue(outputBoundary.successCalled);
        assertNotNull(outputBoundary.outputData);
        assertTrue(outputBoundary.outputData.getReferencingPosts().isEmpty());
    }

    @Test
    void testReadPostOutputDataConstructorWithoutReferencedPost() {
        // Test the constructor that takes 7 parameters (without referencedPost)
        ReadPostOutputData outputData = new ReadPostOutputData(
                1L, "Title", "Content", "user", 5, 2, new ArrayList<>()
        );

        assertEquals(1L, outputData.getId());
        assertEquals("Title", outputData.getTitle());
        assertNull(outputData.getReferencedPost());
        assertTrue(outputData.getReferencingPosts().isEmpty());
    }

    @Test
    void testReadPostOutputDataConstructorWithReferencedPost() {
        // Test the constructor that takes 8 parameters (with referencedPost, without referencingPosts)
        ReadPostOutputData.ReferencedPostData refPost = new ReadPostOutputData.ReferencedPostData(
                2L, "Ref Title", "Ref Content", "refUser"
        );
        ReadPostOutputData outputData = new ReadPostOutputData(
                1L, "Title", "Content", "user", 5, 2, new ArrayList<>(), refPost
        );

        assertEquals(1L, outputData.getId());
        assertNotNull(outputData.getReferencedPost());
        assertEquals(2L, outputData.getReferencedPost().getId());
        assertTrue(outputData.getReferencingPosts().isEmpty());
    }

    // Test helper classes

    private static class TestReadPostDataAccess implements ReadPostDataAccessInterface {
        private final Map<Long, Post> posts = new HashMap<>();
        boolean shouldThrowException = false;
        boolean shouldThrowOnGetAllPosts = false;

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

        @Override
        public java.util.List<OriginalPost> getAllPosts() {
            if (shouldThrowOnGetAllPosts) {
                throw new RuntimeException("getAllPosts error");
            }
            java.util.List<OriginalPost> result = new ArrayList<>();
            for (Post post : posts.values()) {
                if (post instanceof OriginalPost) {
                    result.add((OriginalPost) post);
                }
            }
            return result;
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
