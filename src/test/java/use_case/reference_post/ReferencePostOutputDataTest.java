package use_case.reference_post;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ReferencePostOutputData.
 */
public class ReferencePostOutputDataTest {

    @Test
    void constructorForSearchResultsTest() {
        List<ReferencePostOutputData.PostSearchResult> results = new ArrayList<>();
        results.add(new ReferencePostOutputData.PostSearchResult("1", "Title", "Content", "user1", "2024-01-01 12:00:00"));
        results.add(new ReferencePostOutputData.PostSearchResult("2", "Title2", "Content2", "user2", "2024-01-02 12:00:00"));
        
        ReferencePostOutputData outputData = new ReferencePostOutputData(results, "current1");
        
        assertNotNull(outputData.getSearchResults());
        assertEquals(2, outputData.getSearchResults().size());
        assertEquals("current1", outputData.getCurrentPostId());
        assertNull(outputData.getReferencedPost());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void constructorForSuccessfulReferenceTest() {
        ReferencePostOutputData.PostSearchResult referencedPost = 
            new ReferencePostOutputData.PostSearchResult("1", "Title", "Content", "user1", "2024-01-01 12:00:00");
        
        ReferencePostOutputData outputData = new ReferencePostOutputData(referencedPost, "current1", false);
        
        assertNull(outputData.getSearchResults());
        assertNotNull(outputData.getReferencedPost());
        assertEquals("current1", outputData.getCurrentPostId());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void constructorWithUseCaseFailedTest() {
        ReferencePostOutputData.PostSearchResult referencedPost = 
            new ReferencePostOutputData.PostSearchResult("1", "Title", "Content", "user1", "2024-01-01 12:00:00");
        
        ReferencePostOutputData outputData = new ReferencePostOutputData(referencedPost, "current1", true);
        
        assertTrue(outputData.isUseCaseFailed());
    }

    @Test
    void postSearchResultGettersTest() {
        ReferencePostOutputData.PostSearchResult result = 
            new ReferencePostOutputData.PostSearchResult("123", "Test Title", "Test Content", "testuser", "2024-01-01 12:00:00");
        
        assertEquals("123", result.getPostId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("testuser", result.getCreatorUsername());
        assertEquals("2024-01-01 12:00:00", result.getCreationDate());
    }

    @Test
    void postSearchResultWithEmptyTitleTest() {
        // Reply posts have empty titles
        ReferencePostOutputData.PostSearchResult result = 
            new ReferencePostOutputData.PostSearchResult("1", "", "Content", "user", "2024-01-01 12:00:00");
        
        assertEquals("", result.getTitle());
        assertEquals("Content", result.getContent());
    }

    @Test
    void emptySearchResultsTest() {
        List<ReferencePostOutputData.PostSearchResult> emptyResults = new ArrayList<>();
        ReferencePostOutputData outputData = new ReferencePostOutputData(emptyResults, "current1");
        
        assertNotNull(outputData.getSearchResults());
        assertTrue(outputData.getSearchResults().isEmpty());
    }
}

