package use_case.browse_posts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.OriginalPost;

class BrowsePostsInteractorTest {

    private TestBrowsePostsDataAccess dataAccess;
    private TestBrowsePostsOutputBoundary outputBoundary;
    private BrowsePostsInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestBrowsePostsDataAccess();
        outputBoundary = new TestBrowsePostsOutputBoundary();
        interactor = new BrowsePostsInteractor(dataAccess, outputBoundary);
    }

    @Test
    void executeSuccessWithPosts() {
        // Arrange
        final Date date = new Date();
        final OriginalPost post1 = new OriginalPost(
                1L, "Title 1", "Content 1", "user1", date, 5, 2);
        final OriginalPost post2 = new OriginalPost(
                2L, "Title 2", "Content 2", "user2", date, 10, 3);
        dataAccess.addPost(post1);
        dataAccess.addPost(post2);

        // Act
        interactor.execute();

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertFalse(outputBoundary.isFailCalled());
        assertNotNull(outputBoundary.getOutputData());
        assertEquals(2, outputBoundary.getOutputData().getPosts().size());

        final BrowsePostsOutputData.PostData firstPost = outputBoundary.getOutputData().getPosts().get(0);
        assertEquals(2L, firstPost.getId());
        assertEquals("Title 2", firstPost.getTitle());
        assertEquals("Content 2", firstPost.getContent());
        assertEquals("user2", firstPost.getUsername());
        assertNotNull(firstPost.getCreationDate());
        assertEquals(10, firstPost.getUpvotes());
        assertEquals(3, firstPost.getDownvotes());
    }

    @Test
    void executeSuccessWithEmptyPosts() {
        // Act
        interactor.execute();

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertFalse(outputBoundary.isFailCalled());
        assertNotNull(outputBoundary.getOutputData());
        assertTrue(outputBoundary.getOutputData().getPosts().isEmpty());
    }

    @Test
    void executeFailureWhenExceptionThrown() {
        // Arrange
        dataAccess.setShouldThrowException(true);

        // Act
        interactor.execute();

        // Assert
        assertFalse(outputBoundary.isSuccessCalled());
        assertTrue(outputBoundary.isFailCalled());
        assertTrue(outputBoundary.getErrorMessage().contains("Failed to load posts:"));
    }

    @Test
    void testPostsSortedByScoreDescending() {
        // Arrange - create posts with different vote scores
        final Date date = new Date();
        final OriginalPost lowScore = new OriginalPost(
                1L, "Low", "Content", "user1", date, 1, 5);
        final OriginalPost midScore = new OriginalPost(
                2L, "Mid", "Content", "user2", date, 5, 5);
        final OriginalPost highScore = new OriginalPost(
                3L, "High", "Content", "user3", date, 10, 2);
        dataAccess.addPost(lowScore);
        dataAccess.addPost(midScore);
        dataAccess.addPost(highScore);

        // Act
        interactor.execute();

        // Assert - should be sorted by score descending
        final List<BrowsePostsOutputData.PostData> posts = outputBoundary.getOutputData().getPosts();
        assertEquals(3L, posts.get(0).getId());
        assertEquals(2L, posts.get(1).getId());
        assertEquals(1L, posts.get(2).getId());
    }

    @Test
    void testSwitchToCreatePostView() {
        // Act
        interactor.switchToCreatePostView();

        // Assert
        assertTrue(outputBoundary.isSwitchToCreatePostViewCalled());
    }

    @Test
    void testPostWithReferenceToOriginalPost() {
        // Arrange - create a post that references another original post
        final Date date = new Date();
        final OriginalPost referencedPost = new OriginalPost(
                1L, "Referenced Title", "Referenced Content", "user1", date, 5, 2);
        final OriginalPost postWithReference = new OriginalPost(
                2L, "Title 2", "Content 2", "user2", date, 3, 1);
        postWithReference.setReferencedPost(referencedPost);
        dataAccess.addPost(referencedPost);
        dataAccess.addPost(postWithReference);

        // Act
        interactor.execute();

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        assertEquals(2, outputBoundary.getOutputData().getPosts().size());

        final BrowsePostsOutputData.PostData refPost = outputBoundary.getOutputData().getPosts().stream()
                .filter(post -> post.getId() == 2L)
                .findFirst()
                .orElse(null);
        assertNotNull(refPost);
        assertTrue(refPost.hasReference());
        assertEquals("Referenced Title", refPost.getReferencedPostTitle());
        assertEquals(Long.valueOf(1L), refPost.getReferencedPostId());
    }

    @Test
    void testPostWithReferenceToReplyPost() {
        // Arrange - create a post that references a reply post
        final Date date = new Date();
        final OriginalPost originalPost = new OriginalPost(
                1L, "Original Title", "Original Content", "user1", date, 5, 2);
        final entities.ReplyPost replyPost = new entities.ReplyPost(
                10L, "replyUser", "This is a reply content that is quite long", date, 1, 0);
        originalPost.addReply(replyPost);

        final OriginalPost postWithReference = new OriginalPost(
                2L, "Title 2", "Content 2", "user2", date, 3, 1);
        postWithReference.setReferencedPost(replyPost);
        dataAccess.addPost(originalPost);
        dataAccess.addPost(postWithReference);

        // Act
        interactor.execute();

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        final BrowsePostsOutputData.PostData refPost = outputBoundary.getOutputData().getPosts().stream()
                .filter(post -> post.getId() == 2L)
                .findFirst()
                .orElse(null);
        assertNotNull(refPost);
        assertTrue(refPost.hasReference());
        // For reply posts, the title should be the content (or truncated content)
        assertNotNull(refPost.getReferencedPostTitle());
        assertEquals(Long.valueOf(10L), refPost.getReferencedPostId());
    }

    @Test
    void testPostWithReferenceToReplyPostLongContent() {
        // Arrange - create a post that references a reply with content > 50 chars
        final Date date = new Date();
        final String longContent =
                "This is a very long reply content that exceeds fifty characters and should be truncated";
        final entities.ReplyPost replyPost = new entities.ReplyPost(
                10L, "replyUser", longContent, date, 1, 0);

        final OriginalPost postWithReference = new OriginalPost(
                2L, "Title 2", "Content 2", "user2", date, 3, 1);
        postWithReference.setReferencedPost(replyPost);
        dataAccess.addPost(postWithReference);

        // Act
        interactor.execute();

        // Assert
        final BrowsePostsOutputData.PostData refPost = outputBoundary.getOutputData().getPosts().get(0);
        assertTrue(refPost.hasReference());
        // Should be truncated to 50 chars + "..."
        assertTrue(refPost.getReferencedPostTitle().endsWith("..."));
        assertEquals(53, refPost.getReferencedPostTitle().length());
    }

    @Test
    void testPostWithReferenceToReplyPostShortContent() {
        // Arrange - create a post that references a reply with content <= 50 chars
        final Date date = new Date();
        final String shortContent = "Short reply";
        final entities.ReplyPost replyPost = new entities.ReplyPost(
                10L, "replyUser", shortContent, date, 1, 0);

        final OriginalPost postWithReference = new OriginalPost(
                2L, "Title 2", "Content 2", "user2", date, 3, 1);
        postWithReference.setReferencedPost(replyPost);
        dataAccess.addPost(postWithReference);

        // Act
        interactor.execute();

        // Assert
        final BrowsePostsOutputData.PostData refPost = outputBoundary.getOutputData().getPosts().get(0);
        assertTrue(refPost.hasReference());
        // Should NOT be truncated
        assertEquals(shortContent, refPost.getReferencedPostTitle());
        assertFalse(refPost.getReferencedPostTitle().endsWith("..."));
    }

    @Test
    void testPostDataConstructorWithoutReference() {
        final Date date = new Date();
        final BrowsePostsOutputData.PostData postData = new BrowsePostsOutputData.PostData(
                1L, "Title", "Content", "user", date, 5, 2
        );

        assertEquals(1L, postData.getId());
        assertEquals("Title", postData.getTitle());
        assertEquals("Content", postData.getContent());
        assertEquals("user", postData.getUsername());
        assertEquals(date, postData.getCreationDate());
        assertEquals(5, postData.getUpvotes());
        assertEquals(2, postData.getDownvotes());
        assertFalse(postData.hasReference());
        assertNull(postData.getReferencedPostTitle());
        assertNull(postData.getReferencedPostId());
    }

    @Test
    void testPostWithNoReference() {
        // Arrange - post where hasReference() returns false
        final Date date = new Date();
        final OriginalPost postWithoutRef = new OriginalPost(
                1L, "Title", "Content", "user", date, 5, 2);
        // Don't set any reference - hasReference() should return false
        dataAccess.addPost(postWithoutRef);

        // Act
        interactor.execute();

        // Assert - covers the branch where hasReference is false
        assertTrue(outputBoundary.isSuccessCalled());
        final BrowsePostsOutputData.PostData post = outputBoundary.getOutputData().getPosts().get(0);
        assertFalse(post.hasReference());
        assertNull(post.getReferencedPostTitle());
        assertNull(post.getReferencedPostId());
    }

    @Test
    void testPostWithHasReferenceTrueButNullReferencedPost() {
        // Arrange - covers the branch where hasReference=true but getReferencedPost()=null
        final Date date = new Date();
        final OriginalPost postWithNullRef = new OriginalPost(
                1L, "Title", "Content", "user", date, 5, 2) {
            @Override
            public boolean hasReference() {
                return true;
            }

            @Override
            public entities.Post getReferencedPost() {
                return null;
            }
        };
        dataAccess.addPost(postWithNullRef);

        // Act
        interactor.execute();

        // Assert
        assertTrue(outputBoundary.isSuccessCalled());
        final BrowsePostsOutputData.PostData post = outputBoundary.getOutputData().getPosts().get(0);
        assertTrue(post.hasReference());
        assertNull(post.getReferencedPostTitle());
        assertNull(post.getReferencedPostId());
    }

    // Test helper classes

    private static final class TestBrowsePostsDataAccess implements BrowsePostsDataAccessInterface {
        private final List<OriginalPost> posts = new ArrayList<>();
        private boolean shouldThrowException;

        void addPost(OriginalPost post) {
            posts.add(post);
        }

        void setShouldThrowException(boolean value) {
            this.shouldThrowException = value;
        }

        @Override
        public List<OriginalPost> getAllPosts() {
            if (shouldThrowException) {
                throw new RuntimeException("Database error");
            }
            return posts;
        }
    }

    private static final class TestBrowsePostsOutputBoundary implements BrowsePostsOutputBoundary {
        private boolean successCalled;
        private boolean failCalled;
        private boolean switchToCreatePostViewCalled;
        private BrowsePostsOutputData outputData;
        private String errorMessage;

        boolean isSuccessCalled() {
            return successCalled;
        }

        boolean isFailCalled() {
            return failCalled;
        }

        boolean isSwitchToCreatePostViewCalled() {
            return switchToCreatePostViewCalled;
        }

        BrowsePostsOutputData getOutputData() {
            return outputData;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public void prepareSuccessView(BrowsePostsOutputData data) {
            this.successCalled = true;
            this.outputData = data;
        }

        @Override
        public void prepareFailView(String message) {
            this.failCalled = true;
            this.errorMessage = message;
        }

        @Override
        public void switchToCreatePostView() {
            this.switchToCreatePostViewCalled = true;
        }

        @Override
        public void setCreatePostViewModel(
                interface_adapter.create_post.CreatePostViewModel createPostViewModel) {
        }

        @Override
        public void setViewManagerModel(interface_adapter.ViewManagerModel viewManagerModel) {
        }
    }
}
