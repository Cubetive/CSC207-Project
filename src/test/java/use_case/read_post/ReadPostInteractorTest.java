package use_case.read_post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;

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
        final Date date = new Date();
        final OriginalPost post = new OriginalPost(
                1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        dataAccess.addPost(1L, post);

        final ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertFalse(outputBoundary.isFailCalled());
        assertNotNull(outputBoundary.getOutputData());

        // Verify InputData getter (covers ReadPostInputData)
        assertEquals(1L, inputData.getPostId());

        // Verify OutputData getters (covers ReadPostOutputData)
        assertEquals(1L, outputBoundary.getOutputData().getId());
        assertEquals("Test Title", outputBoundary.getOutputData().getTitle());
        assertEquals("Test Content", outputBoundary.getOutputData().getContent());
        assertEquals("testUser", outputBoundary.getOutputData().getUsername());
        assertEquals(5, outputBoundary.getOutputData().getUpvotes());
        assertEquals(2, outputBoundary.getOutputData().getDownvotes());
        assertNotNull(outputBoundary.getOutputData().getReplies());
    }

    @Test
    void executeSuccessWithReplies() {
        // Arrange
        final Date date = new Date();
        final OriginalPost post = new OriginalPost(
                1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        final ReplyPost reply1 = new ReplyPost(2L, "replyUser1", "Reply 1", date, 3, 1);
        final ReplyPost reply2 = new ReplyPost(3L, "replyUser2", "Reply 2", date, 1, 0);
        post.addReply(reply1);
        post.addReply(reply2);
        dataAccess.addPost(1L, post);

        final ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertEquals(2, outputBoundary.getOutputData().getReplies().size());

        // Verify ReplyData getters (covers ReadPostOutputData.ReplyData)
        final ReadPostOutputData.ReplyData firstReply = outputBoundary.getOutputData().getReplies().get(0);
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
        final Date date = new Date();
        final OriginalPost post = new OriginalPost(
                1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        final ReplyPost reply1 = new ReplyPost(2L, "replyUser1", "Reply 1", date, 3, 1);
        final ReplyPost nestedReply = new ReplyPost(3L, "nestedUser", "Nested Reply", date, 1, 0);
        reply1.addReply(nestedReply);
        post.addReply(reply1);
        dataAccess.addPost(1L, post);

        final ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertEquals(1, outputBoundary.getOutputData().getReplies().size());
        assertEquals(1, outputBoundary.getOutputData().getReplies().get(0).getNestedReplies().size());

        final ReadPostOutputData.ReplyData nestedReplyData =
                outputBoundary.getOutputData().getReplies().get(0).getNestedReplies().get(0);
        assertEquals(3L, nestedReplyData.getId());
        assertEquals("nestedUser", nestedReplyData.getUsername());
        assertEquals("Nested Reply", nestedReplyData.getContent());
    }

    @Test
    void executePostNotFound() {
        // Arrange
        final ReadPostInputData inputData = new ReadPostInputData(999L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertEquals("Post not found with ID: 999", outputBoundary.getErrorMessage());
    }

    @Test
    void executeFailureWhenExceptionThrown() {
        // Arrange
        dataAccess.setShouldThrowException(true);
        final ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertTrue(outputBoundary.getErrorMessage().contains("Failed to load post:"));
    }

    @Test
    void testReplyDataSetters() {
        // Test the setUpvotes and setDownvotes methods on ReplyData
        final ReadPostOutputData.ReplyData replyData = new ReadPostOutputData.ReplyData(
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
        final Date date = new Date();
        final OriginalPost referencedPost = new OriginalPost(
                1L, "Referenced Title", "Referenced Content", "refUser", date, 3, 1);
        final OriginalPost post = new OriginalPost(
                2L, "Test Title", "Test Content", "testUser", date, 5, 2);
        post.setReferencedPost(referencedPost);
        dataAccess.addPost(1L, referencedPost);
        dataAccess.addPost(2L, post);

        final ReadPostInputData inputData = new ReadPostInputData(2L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertNotNull(outputBoundary.getOutputData().getReferencedPost());
        assertEquals(1L, outputBoundary.getOutputData().getReferencedPost().getId());
        assertEquals("Referenced Title", outputBoundary.getOutputData().getReferencedPost().getTitle());
        assertEquals("Referenced Content", outputBoundary.getOutputData().getReferencedPost().getContent());
        assertEquals("refUser", outputBoundary.getOutputData().getReferencedPost().getUsername());
    }

    @Test
    void executeSuccessWithReferencedReplyPost() {
        // Arrange - create a post that references a reply post
        final Date date = new Date();
        final ReplyPost referencedReply = new ReplyPost(10L, "replyUser", "Reply Content", date, 1, 0);
        final OriginalPost post = new OriginalPost(
                2L, "Test Title", "Test Content", "testUser", date, 5, 2);
        post.setReferencedPost(referencedReply);
        dataAccess.addPost(2L, post);

        final ReadPostInputData inputData = new ReadPostInputData(2L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertNotNull(outputBoundary.getOutputData().getReferencedPost());
        assertEquals(10L, outputBoundary.getOutputData().getReferencedPost().getId());
        // For reply posts, title should be empty string
        assertEquals("", outputBoundary.getOutputData().getReferencedPost().getTitle());
        assertEquals("Reply Content", outputBoundary.getOutputData().getReferencedPost().getContent());
    }

    @Test
    void executeSuccessWithReferencingPosts() {
        // Arrange - create a post and another post that references it
        final Date date = new Date();
        final OriginalPost mainPost = new OriginalPost(
                1L, "Main Title", "Main Content", "mainUser", date, 5, 2);
        final OriginalPost referencingPost = new OriginalPost(
                2L, "Referencing Title", "Referencing Content", "refUser", date, 3, 1);
        referencingPost.setReferencedPost(mainPost);
        dataAccess.addPost(1L, mainPost);
        dataAccess.addPost(2L, referencingPost);

        final ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertNotNull(outputBoundary.getOutputData().getReferencingPosts());
        assertEquals(1, outputBoundary.getOutputData().getReferencingPosts().size());

        final ReadPostOutputData.ReferencingPostData refData =
                outputBoundary.getOutputData().getReferencingPosts().get(0);
        assertEquals(2L, refData.getId());
        assertEquals("Referencing Title", refData.getTitle());
        assertEquals("Referencing Content", refData.getContent());
        assertEquals("refUser", refData.getUsername());
    }

    @Test
    void executeSuccessNoReferencingPosts() {
        // Arrange - create a post with no references to it
        final Date date = new Date();
        final OriginalPost post = new OriginalPost(
                1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        dataAccess.addPost(1L, post);

        final ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertNotNull(outputBoundary.getOutputData().getReferencingPosts());
        assertTrue(outputBoundary.getOutputData().getReferencingPosts().isEmpty());
    }

    @Test
    void executeWhenGetAllPostsThrowsException() {
        // Arrange - create a data access that throws on getAllPosts
        final TestReadPostDataAccess failingDataAccess = new TestReadPostDataAccess();
        failingDataAccess.setShouldThrowOnGetAllPosts(true);
        final ReadPostInteractor testInteractor = new ReadPostInteractor(failingDataAccess, outputBoundary);

        final Date date = new Date();
        final OriginalPost post = new OriginalPost(
                1L, "Test Title", "Test Content", "testUser", date, 5, 2);
        failingDataAccess.addPost(1L, post);

        final ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        testInteractor.execute(inputData);

        // Assert - should still succeed, just without referencing posts
        assertTrue(outputBoundary.isSuccessCalled());
        assertNotNull(outputBoundary.getOutputData());
        assertTrue(outputBoundary.getOutputData().getReferencingPosts().isEmpty());
    }

    @Test
    void testReadPostOutputDataConstructorWithoutReferencedPost() {
        // Test the constructor that takes 7 parameters (without referencedPost)
        final ReadPostOutputData outputData = new ReadPostOutputData(
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
        final ReadPostOutputData.ReferencedPostData refPost = new ReadPostOutputData.ReferencedPostData(
                2L, "Ref Title", "Ref Content", "refUser"
        );
        final ReadPostOutputData outputData = new ReadPostOutputData(
                1L, "Title", "Content", "user", 5, 2, new ArrayList<>(), refPost
        );

        assertEquals(1L, outputData.getId());
        assertNotNull(outputData.getReferencedPost());
        assertEquals(2L, outputData.getReferencedPost().getId());
        assertTrue(outputData.getReferencingPosts().isEmpty());
    }

    @Test
    void executeWithReferencingPostHasNullReference() {
        // Arrange - create a post and another post that hasReference() returns true
        // but getReferencedPost() returns null (edge case)
        final Date date = new Date();
        final OriginalPost mainPost = new OriginalPost(
                1L, "Main Title", "Main Content", "mainUser", date, 5, 2);

        // Create a mock post that returns true for hasReference but null for getReferencedPost
        final OriginalPost referencingPost = new OriginalPost(
                2L, "Ref Title", "Ref Content", "refUser", date, 3, 1) {
            @Override
            public boolean hasReference() {
                return true;
            }

            @Override
            public Post getReferencedPost() {
                return null;
            }
        };

        dataAccess.addPost(1L, mainPost);
        dataAccess.addPost(2L, referencingPost);

        final ReadPostInputData inputData = new ReadPostInputData(1L);

        // Act
        interactor.execute(inputData);

        // Assert - should still succeed, the post with null reference should be skipped
        assertTrue(outputBoundary.isSuccessCalled());
        assertNotNull(outputBoundary.getOutputData().getReferencingPosts());
        // The referencing post should NOT be in the list since getReferencedPost() returns null
        assertTrue(outputBoundary.getOutputData().getReferencingPosts().isEmpty());
    }

    // Test helper classes

    private static final class TestReadPostDataAccess implements ReadPostDataAccessInterface {
        private final Map<Long, Post> posts = new HashMap<>();
        private boolean shouldThrowException;
        private boolean shouldThrowOnGetAllPosts;

        void addPost(Long id, Post post) {
            posts.put(id, post);
        }

        void setShouldThrowException(boolean value) {
            this.shouldThrowException = value;
        }

        void setShouldThrowOnGetAllPosts(boolean value) {
            this.shouldThrowOnGetAllPosts = value;
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
            final java.util.List<OriginalPost> result = new ArrayList<>();
            for (Post post : posts.values()) {
                if (post instanceof OriginalPost) {
                    result.add((OriginalPost) post);
                }
            }
            return result;
        }
    }

    private static final class TestReadPostOutputBoundary implements ReadPostOutputBoundary {
        private boolean successCalled;
        private boolean failCalled;
        private ReadPostOutputData outputData;
        private String errorMessage;

        boolean isSuccessCalled() {
            return successCalled;
        }

        boolean isFailCalled() {
            return failCalled;
        }

        ReadPostOutputData getOutputData() {
            return outputData;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public void prepareSuccessView(ReadPostOutputData data) {
            this.successCalled = true;
            this.outputData = data;
        }

        @Override
        public void prepareFailView(String message) {
            this.failCalled = true;
            this.errorMessage = message;
        }
    }
}
