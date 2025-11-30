package interface_adapter.reference_post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.reference_post.ReferencePostInputBoundary;
import use_case.reference_post.ReferencePostInputData;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ReferencePostController.
 */
public class ReferencePostControllerTest {

    private ReferencePostController controller;
    private TestReferencePostInteractor testInteractor;

    @BeforeEach
    void setUp() {
        testInteractor = new TestReferencePostInteractor();
        controller = new ReferencePostController(testInteractor);
    }

    @Test
    void searchPostsTest() {
        controller.searchPosts("1", "test keyword");
        
        assertTrue(testInteractor.searchPostsCalled);
        assertNotNull(testInteractor.lastInputData);
        assertEquals("1", testInteractor.lastInputData.getCurrentPostId());
        assertEquals("test keyword", testInteractor.lastInputData.getSearchKeyword());
        assertNull(testInteractor.lastInputData.getReferencedPostId());
    }

    @Test
    void referencePostTest() {
        controller.referencePost("1", "2");
        
        assertTrue(testInteractor.referencePostCalled);
        assertNotNull(testInteractor.lastInputData);
        assertEquals("1", testInteractor.lastInputData.getCurrentPostId());
        assertEquals("2", testInteractor.lastInputData.getReferencedPostId());
        assertNull(testInteractor.lastInputData.getSearchKeyword());
    }

    @Test
    void cancelReferencePostTest() {
        controller.cancelReferencePost();
        
        assertTrue(testInteractor.cancelReferencePostCalled);
    }

    @Test
    void searchPostsWithNullKeywordTest() {
        controller.searchPosts("1", null);
        
        assertTrue(testInteractor.searchPostsCalled);
        assertNull(testInteractor.lastInputData.getSearchKeyword());
    }

    @Test
    void referencePostWithNullIdsTest() {
        controller.referencePost(null, null);
        
        assertTrue(testInteractor.referencePostCalled);
        assertNull(testInteractor.lastInputData.getCurrentPostId());
        assertNull(testInteractor.lastInputData.getReferencedPostId());
    }

    /**
     * Test implementation of ReferencePostInputBoundary to verify controller calls.
     */
    private static class TestReferencePostInteractor implements ReferencePostInputBoundary {
        boolean searchPostsCalled = false;
        boolean referencePostCalled = false;
        boolean cancelReferencePostCalled = false;
        ReferencePostInputData lastInputData = null;

        @Override
        public void searchPosts(ReferencePostInputData referencePostInputData) {
            searchPostsCalled = true;
            lastInputData = referencePostInputData;
        }

        @Override
        public void referencePost(ReferencePostInputData referencePostInputData) {
            referencePostCalled = true;
            lastInputData = referencePostInputData;
        }

        @Override
        public void cancelReferencePost() {
            cancelReferencePostCalled = true;
        }
    }
}

