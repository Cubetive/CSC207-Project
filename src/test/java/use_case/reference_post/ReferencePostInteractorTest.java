package use_case.reference_post;

import data_access.InMemoryPostDataAccessObject;
import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ReferencePostInteractor.
 */
public class ReferencePostInteractorTest {

    private InMemoryPostDataAccessObject postRepository;
    private ReferencePostInteractor interactor;

    @BeforeEach
    void setUp() {
        postRepository = new InMemoryPostDataAccessObject();
    }

    // ========== Search Posts Tests ==========

    @Test
    void successSearchPostsTest() {
        // Create test posts
        OriginalPost post1 = new OriginalPost("user1", "Test Title", "This is test content");
        OriginalPost post2 = new OriginalPost("user2", "Another Title", "More test content");
        ReplyPost reply1 = new ReplyPost("user3", "Reply with test keyword");
        
        postRepository.savePost(post1);
        postRepository.savePost(post2);
        postRepository.savePost(reply1);

        // Create input data
        ReferencePostInputData inputData = new ReferencePostInputData("1", "test");

        // Create success presenter
        ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                assertNotNull(outputData.getSearchResults());
                assertEquals(3, outputData.getSearchResults().size());
                assertEquals("1", outputData.getCurrentPostId());
                assertFalse(outputData.isUseCaseFailed());
                
                // Verify search results contain expected posts
                List<ReferencePostOutputData.PostSearchResult> results = outputData.getSearchResults();
                boolean foundPost1 = false, foundPost2 = false, foundReply1 = false;
                for (ReferencePostOutputData.PostSearchResult result : results) {
                    if (result.getContent().contains("This is test content")) {
                        foundPost1 = true;
                        assertEquals("Test Title", result.getTitle());
                        assertEquals("user1", result.getCreatorUsername());
                    }
                    if (result.getContent().contains("More test content")) {
                        foundPost2 = true;
                        assertEquals("Another Title", result.getTitle());
                    }
                    if (result.getContent().contains("Reply with test keyword")) {
                        foundReply1 = true;
                        assertEquals("", result.getTitle()); // Reply posts have no title
                    }
                }
                assertTrue(foundPost1 && foundPost2 && foundReply1);
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("prepareSuccessView should not be called for search.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Search should succeed, but got error: " + errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, successPresenter);
        interactor.searchPosts(inputData);
    }

    @Test
    void searchPostsEmptyKeywordTest() {
        ReferencePostInputData inputData = new ReferencePostInputData("1", "");

        ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("Should fail with empty keyword.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("Should fail with empty keyword.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Search keyword cannot be empty.", errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, failurePresenter);
        interactor.searchPosts(inputData);
    }

    @Test
    void searchPostsNullKeywordTest() {
        ReferencePostInputData inputData = new ReferencePostInputData("1", null);

        ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("Should fail with null keyword.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("Should fail with null keyword.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Search keyword cannot be empty.", errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, failurePresenter);
        interactor.searchPosts(inputData);
    }

    @Test
    void searchPostsWhitespaceKeywordTest() {
        ReferencePostInputData inputData = new ReferencePostInputData("1", "   ");

        ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("Should fail with whitespace-only keyword.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("Should fail with whitespace-only keyword.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Search keyword cannot be empty.", errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, failurePresenter);
        interactor.searchPosts(inputData);
    }

    @Test
    void searchPostsNoResultsTest() {
        // Create a post that doesn't match
        OriginalPost post = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(post);

        ReferencePostInputData inputData = new ReferencePostInputData("1", "nonexistent");

        ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("Should fail with no results.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("Should fail with no results.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No posts found.", errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, failurePresenter);
        interactor.searchPosts(inputData);
    }

    @Test
    void searchPostsCaseInsensitiveTest() {
        OriginalPost post = new OriginalPost("user1", "Test Title", "Content");
        postRepository.savePost(post);

        ReferencePostInputData inputData = new ReferencePostInputData("1", "TEST");

        ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                assertNotNull(outputData.getSearchResults());
                assertEquals(1, outputData.getSearchResults().size());
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("prepareSuccessView should not be called for search.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Search should succeed with case-insensitive matching.");
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, successPresenter);
        interactor.searchPosts(inputData);
    }

    // ========== Reference Post Tests ==========

    @Test
    void successReferencePostTest() {
        // Create posts
        OriginalPost referencedPost = new OriginalPost("user1", "Referenced Title", "Referenced content");
        OriginalPost currentPost = new OriginalPost("user2", "Current Title", "Current content");
        
        postRepository.savePost(referencedPost);
        postRepository.savePost(currentPost);

        // Verify initial state
        assertFalse(currentPost.hasReference());
        assertNull(currentPost.getReferencedPost());

        ReferencePostInputData inputData = new ReferencePostInputData(
            String.valueOf(currentPost.getId()),
            null,
            String.valueOf(referencedPost.getId())
        );

        ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("prepareSearchResultsView should not be called.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                assertNotNull(outputData.getReferencedPost());
                assertEquals(String.valueOf(currentPost.getId()), outputData.getCurrentPostId());
                assertFalse(outputData.isUseCaseFailed());
                
                ReferencePostOutputData.PostSearchResult result = outputData.getReferencedPost();
                assertEquals(String.valueOf(referencedPost.getId()), result.getPostId());
                assertEquals("Referenced Title", result.getTitle());
                assertEquals("Referenced content", result.getContent());
                assertEquals("user1", result.getCreatorUsername());
                assertNotNull(result.getCreationDate());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Reference should succeed, but got error: " + errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, successPresenter);
        interactor.referencePost(inputData);

        // Verify the reference was set
        Post savedPost = postRepository.getPostById(String.valueOf(currentPost.getId()));
        assertNotNull(savedPost);
        assertTrue(savedPost.hasReference());
        assertNotNull(savedPost.getReferencedPost());
        assertEquals(referencedPost.getId(), savedPost.getReferencedPost().getId());
    }

    @Test
    void referencePostWithReplyPostTest() {
        // Test referencing a ReplyPost (which has no title)
        ReplyPost referencedPost = new ReplyPost("user1", "Reply content");
        OriginalPost currentPost = new OriginalPost("user2", "Current Title", "Current content");
        
        postRepository.savePost(referencedPost);
        postRepository.savePost(currentPost);

        ReferencePostInputData inputData = new ReferencePostInputData(
            String.valueOf(currentPost.getId()),
            null,
            String.valueOf(referencedPost.getId())
        );

        ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("prepareSearchResultsView should not be called.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                ReferencePostOutputData.PostSearchResult result = outputData.getReferencedPost();
                assertEquals("", result.getTitle()); // Reply posts have no title
                assertEquals("Reply content", result.getContent());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Reference should succeed, but got error: " + errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, successPresenter);
        interactor.referencePost(inputData);
    }

    @Test
    void referencePostEmptyReferencedPostIdTest() {
        OriginalPost currentPost = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(currentPost);

        ReferencePostInputData inputData = new ReferencePostInputData(
            String.valueOf(currentPost.getId()),
            null,
            ""
        );

        ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("Should fail with empty referenced post ID.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("Should fail with empty referenced post ID.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Referenced post ID cannot be empty.", errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, failurePresenter);
        interactor.referencePost(inputData);
    }

    @Test
    void referencePostNullReferencedPostIdTest() {
        OriginalPost currentPost = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(currentPost);

        ReferencePostInputData inputData = new ReferencePostInputData(
            String.valueOf(currentPost.getId()),
            null,
            null
        );

        ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("Should fail with null referenced post ID.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("Should fail with null referenced post ID.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Referenced post ID cannot be empty.", errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, failurePresenter);
        interactor.referencePost(inputData);
    }

    @Test
    void referencePostReferencedPostNotFoundTest() {
        OriginalPost currentPost = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(currentPost);

        ReferencePostInputData inputData = new ReferencePostInputData(
            String.valueOf(currentPost.getId()),
            null,
            "999"
        );

        ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("Should fail when referenced post not found.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("Should fail when referenced post not found.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Referenced post not found.", errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, failurePresenter);
        interactor.referencePost(inputData);
    }

    @Test
    void referencePostCurrentPostNotFoundTest() {
        OriginalPost referencedPost = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(referencedPost);

        ReferencePostInputData inputData = new ReferencePostInputData(
            "999",
            null,
            String.valueOf(referencedPost.getId())
        );

        ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("Should fail when current post not found.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("Should fail when current post not found.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Current post not found.", errorMessage);
            }

            @Override
            public void cancelReferencePost() {
                fail("cancelReferencePost should not be called.");
            }
        };

        interactor = new ReferencePostInteractor(postRepository, failurePresenter);
        interactor.referencePost(inputData);
    }

    // ========== Cancel Reference Post Tests ==========

    @Test
    void cancelReferencePostTest() {
        ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("prepareSearchResultsView should not be called.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                fail("prepareSuccessView should not be called.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("prepareFailView should not be called.");
            }

            @Override
            public void cancelReferencePost() {
                // This is the expected behavior
                assertTrue(true);
            }
        };

        interactor = new ReferencePostInteractor(postRepository, successPresenter);
        interactor.cancelReferencePost();
    }
}

