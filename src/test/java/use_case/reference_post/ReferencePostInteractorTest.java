package use_case.reference_post;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.InMemoryPostDataAccessObject;
import entities.OriginalPost;
import entities.Post;
import entities.ReplyPost;

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
        final OriginalPost post1 = new OriginalPost("user1", "Test Title", "This is test content");
        final OriginalPost post2 = new OriginalPost("user2", "Another Title", "More test content");
        final ReplyPost reply1 = new ReplyPost("user3", "Reply with test keyword");

        postRepository.savePost(post1);
        postRepository.savePost(post2);
        postRepository.savePost(reply1);

        // Create input data
        final ReferencePostInputData inputData = new ReferencePostInputData("1", "test");

        // Create success presenter
        final ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                assertNotNull(outputData.getSearchResults());
                assertEquals(3, outputData.getSearchResults().size());
                assertEquals("1", outputData.getCurrentPostId());
                assertFalse(outputData.isUseCaseFailed());

                // Verify search results contain expected posts
                final List<ReferencePostOutputData.PostSearchResult> results =
                        outputData.getSearchResults();
                boolean foundPost1 = false;
                boolean foundPost2 = false;
                boolean foundReply1 = false;
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
                        // Reply posts have no title
                        assertEquals("", result.getTitle());
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
        final ReferencePostInputData inputData = new ReferencePostInputData("1", "");

        final ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
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
        final ReferencePostInputData inputData = new ReferencePostInputData("1", null);

        final ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
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
        final ReferencePostInputData inputData = new ReferencePostInputData("1", "   ");

        final ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
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
        // Create a post that does not match
        final OriginalPost post = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(post);

        final ReferencePostInputData inputData =
                new ReferencePostInputData("1", "nonexistent");

        final ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
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
        final OriginalPost post = new OriginalPost("user1", "Test Title", "Content");
        postRepository.savePost(post);

        final ReferencePostInputData inputData = new ReferencePostInputData("1", "TEST");

        final ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
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
        final OriginalPost referencedPost =
                new OriginalPost("user1", "Referenced Title", "Referenced content");
        final OriginalPost currentPost =
                new OriginalPost("user2", "Current Title", "Current content");

        postRepository.savePost(referencedPost);
        postRepository.savePost(currentPost);

        // Verify initial state
        assertFalse(currentPost.hasReference());
        assertNull(currentPost.getReferencedPost());

        final ReferencePostInputData inputData = new ReferencePostInputData(
                String.valueOf(currentPost.getId()),
                null,
                String.valueOf(referencedPost.getId())
        );

        final ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("prepareSearchResultsView should not be called.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                assertNotNull(outputData.getReferencedPost());
                assertEquals(String.valueOf(currentPost.getId()), outputData.getCurrentPostId());
                assertFalse(outputData.isUseCaseFailed());

                final ReferencePostOutputData.PostSearchResult result =
                        outputData.getReferencedPost();
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
        final Post savedPost = postRepository.getPostById(String.valueOf(currentPost.getId()));
        assertNotNull(savedPost);
        assertTrue(savedPost.hasReference());
        assertNotNull(savedPost.getReferencedPost());
        assertEquals(referencedPost.getId(), savedPost.getReferencedPost().getId());
    }

    @Test
    void referencePostWithReplyPostTest() {
        // Test referencing a ReplyPost (which has no title)
        final ReplyPost referencedPost = new ReplyPost("user1", "Reply content");
        final OriginalPost currentPost =
                new OriginalPost("user2", "Current Title", "Current content");

        postRepository.savePost(referencedPost);
        postRepository.savePost(currentPost);

        final ReferencePostInputData inputData = new ReferencePostInputData(
                String.valueOf(currentPost.getId()),
                null,
                String.valueOf(referencedPost.getId())
        );

        final ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
            @Override
            public void prepareSearchResultsView(ReferencePostOutputData outputData) {
                fail("prepareSearchResultsView should not be called.");
            }

            @Override
            public void prepareSuccessView(ReferencePostOutputData outputData) {
                final ReferencePostOutputData.PostSearchResult result =
                        outputData.getReferencedPost();
                // Reply posts have no title
                assertEquals("", result.getTitle());
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
        final OriginalPost currentPost = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(currentPost);

        final ReferencePostInputData inputData = new ReferencePostInputData(
                String.valueOf(currentPost.getId()),
                null,
                ""
        );

        final ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
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
        final OriginalPost currentPost = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(currentPost);

        final ReferencePostInputData inputData = new ReferencePostInputData(
                String.valueOf(currentPost.getId()),
                null,
                null
        );

        final ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
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
        final OriginalPost currentPost = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(currentPost);

        final ReferencePostInputData inputData = new ReferencePostInputData(
                String.valueOf(currentPost.getId()),
                null,
                "999"
        );

        final ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
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
        final OriginalPost referencedPost = new OriginalPost("user1", "Title", "Content");
        postRepository.savePost(referencedPost);

        final ReferencePostInputData inputData = new ReferencePostInputData(
                "999",
                null,
                String.valueOf(referencedPost.getId())
        );

        final ReferencePostOutputBoundary failurePresenter = new ReferencePostOutputBoundary() {
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
        final ReferencePostOutputBoundary successPresenter = new ReferencePostOutputBoundary() {
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
