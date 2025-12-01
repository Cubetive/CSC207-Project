package use_case.reference_post;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ReferencePostInputData.
 */
public class ReferencePostInputDataTest {

    @Test
    void constructorWithSearchKeywordTest() {
        ReferencePostInputData inputData = new ReferencePostInputData("1", "test keyword");
        
        assertEquals("1", inputData.getCurrentPostId());
        assertEquals("test keyword", inputData.getSearchKeyword());
        assertNull(inputData.getReferencedPostId());
    }

    @Test
    void constructorWithReferencedPostIdTest() {
        ReferencePostInputData inputData = new ReferencePostInputData("1", null, "2");
        
        assertEquals("1", inputData.getCurrentPostId());
        assertNull(inputData.getSearchKeyword());
        assertEquals("2", inputData.getReferencedPostId());
    }

    @Test
    void constructorWithAllFieldsTest() {
        ReferencePostInputData inputData = new ReferencePostInputData("1", "keyword", "2");
        
        assertEquals("1", inputData.getCurrentPostId());
        assertEquals("keyword", inputData.getSearchKeyword());
        assertEquals("2", inputData.getReferencedPostId());
    }

    @Test
    void gettersTest() {
        ReferencePostInputData inputData = new ReferencePostInputData("current123", "search term", "ref456");
        
        assertEquals("current123", inputData.getCurrentPostId());
        assertEquals("search term", inputData.getSearchKeyword());
        assertEquals("ref456", inputData.getReferencedPostId());
    }

    @Test
    void nullValuesTest() {
        ReferencePostInputData inputData1 = new ReferencePostInputData(null, "keyword");
        assertNull(inputData1.getCurrentPostId());
        
        ReferencePostInputData inputData2 = new ReferencePostInputData("1", null, "2");
        assertNull(inputData2.getSearchKeyword());
    }
}

