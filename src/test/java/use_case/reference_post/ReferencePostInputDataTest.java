package use_case.reference_post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for ReferencePostInputData.
 */
public class ReferencePostInputDataTest {

    @Test
    void constructorWithSearchKeywordTest() {
        final ReferencePostInputData inputData = new ReferencePostInputData("1", "test keyword");

        assertEquals("1", inputData.getCurrentPostId());
        assertEquals("test keyword", inputData.getSearchKeyword());
        assertNull(inputData.getReferencedPostId());
    }

    @Test
    void constructorWithReferencedPostIdTest() {
        final ReferencePostInputData inputData = new ReferencePostInputData("1", null, "2");

        assertEquals("1", inputData.getCurrentPostId());
        assertNull(inputData.getSearchKeyword());
        assertEquals("2", inputData.getReferencedPostId());
    }

    @Test
    void constructorWithAllFieldsTest() {
        final ReferencePostInputData inputData = new ReferencePostInputData("1", "keyword", "2");

        assertEquals("1", inputData.getCurrentPostId());
        assertEquals("keyword", inputData.getSearchKeyword());
        assertEquals("2", inputData.getReferencedPostId());
    }

    @Test
    void gettersTest() {
        final ReferencePostInputData inputData = new ReferencePostInputData("current123", "search term", "ref456");

        assertEquals("current123", inputData.getCurrentPostId());
        assertEquals("search term", inputData.getSearchKeyword());
        assertEquals("ref456", inputData.getReferencedPostId());
    }

    @Test
    void nullValuesTest() {
        final ReferencePostInputData inputData1 = new ReferencePostInputData(null, "keyword");
        assertNull(inputData1.getCurrentPostId());

        final ReferencePostInputData inputData2 = new ReferencePostInputData("1", null, "2");
        assertNull(inputData2.getSearchKeyword());
    }
}
