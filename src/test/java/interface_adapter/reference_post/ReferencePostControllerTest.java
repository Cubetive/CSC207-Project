package interface_adapter.reference_post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import use_case.reference_post.ReferencePostInputBoundary;
import use_case.reference_post.ReferencePostInputData;

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

        assertTrue(testInteractor.isSearchPostsCalled());
        assertNotNull(testInteractor.getLastInputData());
        assertEquals("1", testInteractor.getLastInputData().getCurrentPostId());
        assertEquals("test keyword", testInteractor.getLastInputData().getSearchKeyword());
        assertNull(testInteractor.getLastInputData().getReferencedPostId());
    }

    @Test
    void referencePostTest() {
        controller.referencePost("1", "2");

        assertTrue(testInteractor.isReferencePostCalled());
        assertNotNull(testInteractor.getLastInputData());
        assertEquals("1", testInteractor.getLastInputData().getCurrentPostId());
        assertEquals("2", testInteractor.getLastInputData().getReferencedPostId());
        assertNull(testInteractor.getLastInputData().getSearchKeyword());
    }

    @Test
    void cancelReferencePostTest() {
        controller.cancelReferencePost();

        assertTrue(testInteractor.isCancelReferencePostCalled());
    }

    @Test
    void searchPostsWithNullKeywordTest() {
        controller.searchPosts("1", null);

        assertTrue(testInteractor.isSearchPostsCalled());
        assertNull(testInteractor.getLastInputData().getSearchKeyword());
    }

    @Test
    void referencePostWithNullIdsTest() {
        controller.referencePost(null, null);

        assertTrue(testInteractor.isReferencePostCalled());
        assertNull(testInteractor.getLastInputData().getCurrentPostId());
        assertNull(testInteractor.getLastInputData().getReferencedPostId());
    }

    /**
     * Test implementation of ReferencePostInputBoundary to verify controller calls.
     */
    private static final class TestReferencePostInteractor implements ReferencePostInputBoundary {
        private boolean searchPostsCalled;
        private boolean referencePostCalled;
        private boolean cancelReferencePostCalled;
        private ReferencePostInputData lastInputData;

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

        public boolean isSearchPostsCalled() {
            return searchPostsCalled;
        }

        public boolean isReferencePostCalled() {
            return referencePostCalled;
        }

        public boolean isCancelReferencePostCalled() {
            return cancelReferencePostCalled;
        }

        public ReferencePostInputData getLastInputData() {
            return lastInputData;
        }
    }
}
