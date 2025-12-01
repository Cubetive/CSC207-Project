package use_case.browse_posts;

import entities.OriginalPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        Date date = new Date();
        OriginalPost post1 = new OriginalPost(1L, "Title 1", "Content 1", "user1", date, 5, 2);
        OriginalPost post2 = new OriginalPost(2L, "Title 2", "Content 2", "user2", date, 10, 3);
        dataAccess.addPost(post1);
        dataAccess.addPost(post2);

        // Act
        interactor.execute();

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertFalse(outputBoundary.failCalled);
        assertNotNull(outputBoundary.outputData);
        assertEquals(2, outputBoundary.outputData.getPosts().size());

        BrowsePostsOutputData.PostData firstPost = outputBoundary.outputData.getPosts().get(0);
        assertEquals(2L, firstPost.getId()); // post2 comes first due to higher score (10-3=7 vs 5-2=3)
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
        assertTrue(outputBoundary.successCalled);
        assertFalse(outputBoundary.failCalled);
        assertNotNull(outputBoundary.outputData);
        assertTrue(outputBoundary.outputData.getPosts().isEmpty());
    }

    @Test
    void executeFailureWhenExceptionThrown() {
        // Arrange
        dataAccess.shouldThrowException = true;

        // Act
        interactor.execute();

        // Assert
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failCalled);
        assertTrue(outputBoundary.errorMessage.contains("Failed to load posts:"));
    }

    @Test
    void testPostsSortedByScoreDescending() {
        // Arrange - create posts with different vote scores
        Date date = new Date();
        OriginalPost lowScore = new OriginalPost(1L, "Low", "Content", "user1", date, 1, 5);   // score: -4
        OriginalPost midScore = new OriginalPost(2L, "Mid", "Content", "user2", date, 5, 5);   // score: 0
        OriginalPost highScore = new OriginalPost(3L, "High", "Content", "user3", date, 10, 2); // score: 8
        dataAccess.addPost(lowScore);
        dataAccess.addPost(midScore);
        dataAccess.addPost(highScore);

        // Act
        interactor.execute();

        // Assert - should be sorted by score descending
        List<BrowsePostsOutputData.PostData> posts = outputBoundary.outputData.getPosts();
        assertEquals(3L, posts.get(0).getId()); // highScore first
        assertEquals(2L, posts.get(1).getId()); // midScore second
        assertEquals(1L, posts.get(2).getId()); // lowScore last
    }

    @Test
    void testSwitchToCreatePostView() {
        // Act
        interactor.switchToCreatePostView();

        // Assert
        assertTrue(outputBoundary.switchToCreatePostViewCalled);
    }

    @Test
    void testPostWithReferenceToOriginalPost() {
        // Arrange - create a post that references another original post
        Date date = new Date();
        OriginalPost referencedPost = new OriginalPost(1L, "Referenced Title", "Referenced Content", "user1", date, 5, 2);
        OriginalPost postWithReference = new OriginalPost(2L, "Title 2", "Content 2", "user2", date, 3, 1);
        postWithReference.setReferencedPost(referencedPost);
        dataAccess.addPost(referencedPost);
        dataAccess.addPost(postWithReference);

        // Act
        interactor.execute();

        // Assert
        assertTrue(outputBoundary.successCalled);
        assertEquals(2, outputBoundary.outputData.getPosts().size());

        BrowsePostsOutputData.PostData refPost = outputBoundary.outputData.getPosts().stream()
                .filter(p -> p.getId() == 2L)
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
        Date date = new Date();
        OriginalPost originalPost = new OriginalPost(1L, "Original Title", "Original Content", "user1", date, 5, 2);
        entities.ReplyPost replyPost = new entities.ReplyPost(10L, "replyUser", "This is a reply content that is quite long", date, 1, 0);
        originalPost.addReply(replyPost);

        OriginalPost postWithReference = new OriginalPost(2L, "Title 2", "Content 2", "user2", date, 3, 1);
        postWithReference.setReferencedPost(replyPost);
        dataAccess.addPost(originalPost);
        dataAccess.addPost(postWithReference);

        // Act
        interactor.execute();

        // Assert
        assertTrue(outputBoundary.successCalled);
        BrowsePostsOutputData.PostData refPost = outputBoundary.outputData.getPosts().stream()
                .filter(p -> p.getId() == 2L)
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
        Date date = new Date();
        String longContent = "This is a very long reply content that exceeds fifty characters and should be truncated";
        entities.ReplyPost replyPost = new entities.ReplyPost(10L, "replyUser", longContent, date, 1, 0);

        OriginalPost postWithReference = new OriginalPost(2L, "Title 2", "Content 2", "user2", date, 3, 1);
        postWithReference.setReferencedPost(replyPost);
        dataAccess.addPost(postWithReference);

        // Act
        interactor.execute();

        // Assert
        BrowsePostsOutputData.PostData refPost = outputBoundary.outputData.getPosts().get(0);
        assertTrue(refPost.hasReference());
        // Should be truncated to 50 chars + "..."
        assertTrue(refPost.getReferencedPostTitle().endsWith("..."));
        assertEquals(53, refPost.getReferencedPostTitle().length());
    }

    @Test
    void testPostWithReferenceToReplyPostShortContent() {
        // Arrange - create a post that references a reply with content <= 50 chars
        Date date = new Date();
        String shortContent = "Short reply";
        entities.ReplyPost replyPost = new entities.ReplyPost(10L, "replyUser", shortContent, date, 1, 0);

        OriginalPost postWithReference = new OriginalPost(2L, "Title 2", "Content 2", "user2", date, 3, 1);
        postWithReference.setReferencedPost(replyPost);
        dataAccess.addPost(postWithReference);

        // Act
        interactor.execute();

        // Assert
        BrowsePostsOutputData.PostData refPost = outputBoundary.outputData.getPosts().get(0);
        assertTrue(refPost.hasReference());
        // Should NOT be truncated
        assertEquals(shortContent, refPost.getReferencedPostTitle());
        assertFalse(refPost.getReferencedPostTitle().endsWith("..."));
    }

    @Test
    void testPostDataConstructorWithoutReference() {
        Date date = new Date();
        BrowsePostsOutputData.PostData postData = new BrowsePostsOutputData.PostData(
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
        Date date = new Date();
        OriginalPost postWithoutRef = new OriginalPost(1L, "Title", "Content", "user", date, 5, 2);
        // Don't set any reference - hasReference() should return false
        dataAccess.addPost(postWithoutRef);

        // Act
        interactor.execute();

        // Assert - covers the branch where hasReference is false
        assertTrue(outputBoundary.successCalled);
        BrowsePostsOutputData.PostData post = outputBoundary.outputData.getPosts().get(0);
        assertFalse(post.hasReference());
        assertNull(post.getReferencedPostTitle());
        assertNull(post.getReferencedPostId());
    }

    @Test
    void testPostWithHasReferenceTrueButNullReferencedPost() {
        // Arrange - covers the branch where hasReference=true but getReferencedPost()=null
        Date date = new Date();
        OriginalPost postWithNullRef = new OriginalPost(1L, "Title", "Content", "user", date, 5, 2) {
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
        assertTrue(outputBoundary.successCalled);
        BrowsePostsOutputData.PostData post = outputBoundary.outputData.getPosts().get(0);
        assertTrue(post.hasReference());
        assertNull(post.getReferencedPostTitle());
        assertNull(post.getReferencedPostId());
    }

    // Test helper classes

    private static class TestBrowsePostsDataAccess implements BrowsePostsDataAccessInterface {
        private final List<OriginalPost> posts = new ArrayList<>();
        boolean shouldThrowException = false;

        void addPost(OriginalPost post) {
            posts.add(post);
        }

        @Override
        public List<OriginalPost> getAllPosts() {
            if (shouldThrowException) {
                throw new RuntimeException("Database error");
            }
            return posts;
        }
    }

    private static class TestBrowsePostsOutputBoundary implements BrowsePostsOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        boolean switchToCreatePostViewCalled = false;
        BrowsePostsOutputData outputData;
        String errorMessage;

        @Override
        public void prepareSuccessView(BrowsePostsOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failCalled = true;
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToCreatePostView() {
            this.switchToCreatePostViewCalled = true;
        }

        @Override
        public void setCreatePostViewModel(interface_adapter.create_post.CreatePostViewModel createPostViewModel) {
        }

        @Override
        public void setViewManagerModel(interface_adapter.ViewManagerModel viewManagerModel) {
        }
    }
}
