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

        // Verify OutputData and PostData getters (covers BrowsePostsOutputData)
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
            // Not needed for these tests
        }

        @Override
        public void setViewManagerModel(interface_adapter.ViewManagerModel viewManagerModel) {
            // Not needed for these tests
        }
    }
}
