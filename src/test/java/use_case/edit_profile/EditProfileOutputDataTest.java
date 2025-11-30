package use_case.edit_profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditProfileOutputData.
 */
public class EditProfileOutputDataTest {

    @Test
    void constructorWithSuccessTest() {
        EditProfileOutputData outputData = new EditProfileOutputData(
            "username", "Full Name", "Bio text", "pic.jpg", false
        );
        
        assertEquals("username", outputData.getUsername());
        assertEquals("Full Name", outputData.getFullName());
        assertEquals("Bio text", outputData.getBio());
        assertEquals("pic.jpg", outputData.getProfilePicture());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void constructorWithFailureTest() {
        EditProfileOutputData outputData = new EditProfileOutputData(
            "username", "Full Name", "Bio text", "pic.jpg", true
        );
        
        assertTrue(outputData.isUseCaseFailed());
    }

    @Test
    void constructorWithNullValuesTest() {
        EditProfileOutputData outputData = new EditProfileOutputData(
            "username", "Full Name", null, null, false
        );
        
        assertEquals("username", outputData.getUsername());
        assertEquals("Full Name", outputData.getFullName());
        assertNull(outputData.getBio());
        assertNull(outputData.getProfilePicture());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void gettersTest() {
        EditProfileOutputData outputData = new EditProfileOutputData(
            "testUser", "Test Name", "Test Bio", "test.jpg", false
        );
        
        assertEquals("testUser", outputData.getUsername());
        assertEquals("Test Name", outputData.getFullName());
        assertEquals("Test Bio", outputData.getBio());
        assertEquals("test.jpg", outputData.getProfilePicture());
    }

    @Test
    void emptyStringValuesTest() {
        EditProfileOutputData outputData = new EditProfileOutputData(
            "", "", "", "", false
        );
        
        assertEquals("", outputData.getUsername());
        assertEquals("", outputData.getFullName());
        assertEquals("", outputData.getBio());
        assertEquals("", outputData.getProfilePicture());
    }
}

