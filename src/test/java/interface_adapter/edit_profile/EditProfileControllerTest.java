package interface_adapter.edit_profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.edit_profile.EditProfileInputBoundary;
import use_case.edit_profile.EditProfileInputData;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditProfileController.
 */
public class EditProfileControllerTest {

    private EditProfileController controller;
    private TestEditProfileInteractor testInteractor;

    @BeforeEach
    void setUp() {
        testInteractor = new TestEditProfileInteractor();
        controller = new EditProfileController(testInteractor);
    }

    @Test
    void executeTest() {
        controller.execute(
            "currentUser", "newUser", "John Doe", "Bio", "pic.jpg",
            "oldPass", "newPass", "newPass"
        );
        
        assertTrue(testInteractor.executeCalled);
        assertNotNull(testInteractor.lastInputData);
        assertEquals("currentUser", testInteractor.lastInputData.getCurrentUsername());
        assertEquals("newUser", testInteractor.lastInputData.getNewUsername());
        assertEquals("John Doe", testInteractor.lastInputData.getFullName());
        assertEquals("Bio", testInteractor.lastInputData.getBio());
        assertEquals("pic.jpg", testInteractor.lastInputData.getProfilePicture());
        assertEquals("oldPass", testInteractor.lastInputData.getCurrentPassword());
        assertEquals("newPass", testInteractor.lastInputData.getNewPassword());
        assertEquals("newPass", testInteractor.lastInputData.getRepeatNewPassword());
    }

    @Test
    void executeWithoutPasswordChangeTest() {
        controller.execute(
            "user", "user", "Name", "Bio", null,
            null, null, null
        );
        
        assertTrue(testInteractor.executeCalled);
        assertNotNull(testInteractor.lastInputData);
        assertFalse(testInteractor.lastInputData.isChangingPassword());
        assertFalse(testInteractor.lastInputData.isChangingUsername());
    }

    @Test
    void executeWithPasswordChangeTest() {
        controller.execute(
            "user", "user", "Name", null, null,
            "old", "newpass123", "newpass123"
        );
        
        assertTrue(testInteractor.executeCalled);
        assertTrue(testInteractor.lastInputData.isChangingPassword());
    }

    @Test
    void executeWithUsernameChangeTest() {
        controller.execute(
            "olduser", "newuser", "Name", null, null,
            null, null, null
        );
        
        assertTrue(testInteractor.executeCalled);
        assertTrue(testInteractor.lastInputData.isChangingUsername());
    }

    @Test
    void executeWithNullValuesTest() {
        controller.execute(
            "user", "user", "Name", null, null,
            null, null, null
        );
        
        assertTrue(testInteractor.executeCalled);
        assertNull(testInteractor.lastInputData.getBio());
        assertNull(testInteractor.lastInputData.getProfilePicture());
        assertNull(testInteractor.lastInputData.getCurrentPassword());
        assertNull(testInteractor.lastInputData.getNewPassword());
        assertNull(testInteractor.lastInputData.getRepeatNewPassword());
    }

    @Test
    void executeWithEmptyStringsTest() {
        controller.execute(
            "user", "user", "Name", "", "",
            "", "", ""
        );
        
        assertTrue(testInteractor.executeCalled);
        assertEquals("", testInteractor.lastInputData.getBio());
        assertEquals("", testInteractor.lastInputData.getProfilePicture());
    }

    /**
     * Test implementation of EditProfileInputBoundary to verify controller calls.
     */
    private static class TestEditProfileInteractor implements EditProfileInputBoundary {
        boolean executeCalled = false;
        EditProfileInputData lastInputData = null;

        @Override
        public void execute(EditProfileInputData editProfileInputData) {
            executeCalled = true;
            lastInputData = editProfileInputData;
        }
    }
}

