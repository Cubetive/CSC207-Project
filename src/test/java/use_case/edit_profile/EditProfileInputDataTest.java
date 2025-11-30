package use_case.edit_profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditProfileInputData.
 */
public class EditProfileInputDataTest {

    @Test
    void constructorTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "currentUser", "newUser", "John Doe", "Bio text", "pic.jpg",
            "oldPass", "newPass", "newPass"
        );
        
        assertEquals("currentUser", inputData.getCurrentUsername());
        assertEquals("newUser", inputData.getNewUsername());
        assertEquals("John Doe", inputData.getFullName());
        assertEquals("Bio text", inputData.getBio());
        assertEquals("pic.jpg", inputData.getProfilePicture());
        assertEquals("oldPass", inputData.getCurrentPassword());
        assertEquals("newPass", inputData.getNewPassword());
        assertEquals("newPass", inputData.getRepeatNewPassword());
    }

    @Test
    void constructorWithNullValuesTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "currentUser", "currentUser", "John Doe", null, null,
            null, null, null
        );
        
        assertEquals("currentUser", inputData.getCurrentUsername());
        assertEquals("currentUser", inputData.getNewUsername());
        assertEquals("John Doe", inputData.getFullName());
        assertNull(inputData.getBio());
        assertNull(inputData.getProfilePicture());
        assertNull(inputData.getCurrentPassword());
        assertNull(inputData.getNewPassword());
        assertNull(inputData.getRepeatNewPassword());
    }

    @Test
    void isChangingPasswordWithNewPasswordTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "user", "user", "Name", null, null,
            "old", "newPass", "newPass"
        );
        
        assertTrue(inputData.isChangingPassword());
    }

    @Test
    void isChangingPasswordWithNullNewPasswordTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "user", "user", "Name", null, null,
            null, null, null
        );
        
        assertFalse(inputData.isChangingPassword());
    }

    @Test
    void isChangingPasswordWithEmptyNewPasswordTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "user", "user", "Name", null, null,
            null, "", ""
        );
        
        assertFalse(inputData.isChangingPassword());
    }

    @Test
    void isChangingPasswordWithWhitespaceOnlyNewPasswordTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "user", "user", "Name", null, null,
            null, "   ", "   "
        );
        
        assertFalse(inputData.isChangingPassword());
    }

    @Test
    void isChangingUsernameWithDifferentUsernamesTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "oldUser", "newUser", "Name", null, null,
            null, null, null
        );
        
        assertTrue(inputData.isChangingUsername());
    }

    @Test
    void isChangingUsernameWithSameUsernamesTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "user", "user", "Name", null, null,
            null, null, null
        );
        
        assertFalse(inputData.isChangingUsername());
    }

    @Test
    void gettersTest() {
        EditProfileInputData inputData = new EditProfileInputData(
            "testUser", "testUser2", "Test Name", "Test Bio", "test.jpg",
            "pass123", "pass456", "pass456"
        );
        
        assertEquals("testUser", inputData.getCurrentUsername());
        assertEquals("testUser2", inputData.getNewUsername());
        assertEquals("Test Name", inputData.getFullName());
        assertEquals("Test Bio", inputData.getBio());
        assertEquals("test.jpg", inputData.getProfilePicture());
        assertEquals("pass123", inputData.getCurrentPassword());
        assertEquals("pass456", inputData.getNewPassword());
        assertEquals("pass456", inputData.getRepeatNewPassword());
    }
}

