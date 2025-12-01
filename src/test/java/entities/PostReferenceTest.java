package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Post reference-related methods.
 */
public class PostReferenceTest {

    private OriginalPost post1;
    private OriginalPost post2;
    private ReplyPost reply1;

    @BeforeEach
    void setUp() {
        post1 = new OriginalPost("user1", "Title 1", "Content 1");
        post2 = new OriginalPost("user2", "Title 2", "Content 2");
        reply1 = new ReplyPost("user3", "Reply content");
    }

    @Test
    void initialReferenceStateTest() {
        assertFalse(post1.hasReference());
        assertNull(post1.getReferencedPost());
    }

    @Test
    void setReferencedPostTest() {
        post1.setReferencedPost(post2);
        
        assertTrue(post1.hasReference());
        assertNotNull(post1.getReferencedPost());
        assertEquals(post2.getId(), post1.getReferencedPost().getId());
        assertEquals("Title 2", ((OriginalPost) post1.getReferencedPost()).getTitle());
    }

    @Test
    void setReferencedPostWithReplyPostTest() {
        post1.setReferencedPost(reply1);
        
        assertTrue(post1.hasReference());
        assertNotNull(post1.getReferencedPost());
        assertEquals(reply1.getId(), post1.getReferencedPost().getId());
        assertTrue(post1.getReferencedPost() instanceof ReplyPost);
    }

    @Test
    void setReferencedPostToNullTest() {
        // First set a reference
        post1.setReferencedPost(post2);
        assertTrue(post1.hasReference());
        
        // Then remove it
        post1.setReferencedPost(null);
        assertFalse(post1.hasReference());
        assertNull(post1.getReferencedPost());
    }

    @Test
    void getReferencedPostTest() {
        post1.setReferencedPost(post2);
        
        Post referenced = post1.getReferencedPost();
        assertNotNull(referenced);
        assertEquals(post2.getId(), referenced.getId());
        assertEquals("user2", referenced.getCreatorUsername());
        assertEquals("Content 2", referenced.getContent());
    }

    @Test
    void hasReferenceAfterSettingTest() {
        assertFalse(post1.hasReference());
        
        post1.setReferencedPost(post2);
        assertTrue(post1.hasReference());
        
        post1.setReferencedPost(null);
        assertFalse(post1.hasReference());
    }

    @Test
    void referenceChainTest() {
        // Create a chain: post1 references post2, post2 references reply1
        post1.setReferencedPost(post2);
        post2.setReferencedPost(reply1);
        
        assertTrue(post1.hasReference());
        assertTrue(post2.hasReference());
        assertFalse(reply1.hasReference());
        
        assertEquals(post2.getId(), post1.getReferencedPost().getId());
        assertEquals(reply1.getId(), post2.getReferencedPost().getId());
    }

    @Test
    void referenceWithPostLoadedFromStorageTest() {
        // Simulate a post loaded from storage (using the constructor with all parameters)
        Date creationDate = new Date();
        OriginalPost loadedPost = new OriginalPost(100L, "user1", "Title", "Content", creationDate, 5, 2);
        
        // Initially no reference
        assertFalse(loadedPost.hasReference());
        assertNull(loadedPost.getReferencedPost());
        
        // Set reference
        loadedPost.setReferencedPost(post2);
        assertTrue(loadedPost.hasReference());
        assertEquals(post2.getId(), loadedPost.getReferencedPost().getId());
    }

    @Test
    void replyPostReferenceTest() {
        // Reply posts can also have references
        reply1.setReferencedPost(post1);
        
        assertTrue(reply1.hasReference());
        assertEquals(post1.getId(), reply1.getReferencedPost().getId());
    }

    @Test
    void changeReferenceTest() {
        // Set initial reference
        post1.setReferencedPost(post2);
        assertEquals(post2.getId(), post1.getReferencedPost().getId());
        
        // Change to a different reference
        post1.setReferencedPost(reply1);
        assertEquals(reply1.getId(), post1.getReferencedPost().getId());
        assertNotEquals(post2.getId(), post1.getReferencedPost().getId());
    }
}

