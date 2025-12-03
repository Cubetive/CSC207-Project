package use_case.edit_post;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

import org.junit.jupiter.api.Test;

import entities.User;
import interface_adapter.edit_post.EditPostController;
import interface_adapter.read_post.ReadPostState;

public class EditPostInteractorTest {

    ReadPostState createSamplePost() {
        final ReadPostState state = new ReadPostState();
        state.setUsername("bob123");
        state.setTitle("This is the title");
        state.setContent("this is my original text");
        state.setId(1);
        return state;
    }

    @Test
    void successTestEditPost() {
        final String newText = "this is my new text";
        final JTextArea contentArea = new JTextArea();
        final ReadPostState state = createSamplePost();
        final User curUser = new User("Bob Marley", "bob123", "bobmarl@gmail.com",
                "bobby111222333");
        final JDialog dialog = new JDialog();
        final EditPostController editPostController =
                new EditPostController(new EditPostInputData(contentArea, state.getId(), curUser.getUsername(), state,
                        newText, dialog));
        editPostController.editPost();
        assertEquals(newText, state.getContent());
    }

    @Test
    void failTestEditPostLength() {
        final String original = "this is my original text";
        final String newText = "";
        final JTextArea contentArea = new JTextArea();
        final ReadPostState state = createSamplePost();
        final User curUser = new User("Bob Marley", "bob123", "bobmarl@gmail.com",
                "bobby111222333");
        final JDialog dialog = new JDialog();
        final EditPostController editPostController =
                new EditPostController(new EditPostInputData(contentArea, state.getId(), curUser.getUsername(), state,
                        newText, dialog));
        editPostController.editPost();
        assertEquals(original, state.getContent());
    }

    @Test
    void failTestEditPostUsername() {
        final String original = "this is my original text";
        final String newText = "this is my new text";
        final JTextArea contentArea = new JTextArea();
        final ReadPostState state = createSamplePost();
        final User curUser = new User("Bob Marley", "bob1", "bobmarl@gmail.com",
                "bobby111222333");
        final JDialog dialog = new JDialog();
        final EditPostController editPostController =
                new EditPostController(new EditPostInputData(contentArea, state.getId(), curUser.getUsername(), state,
                        newText, dialog));
        editPostController.editPost();
        assertEquals(original, state.getContent());
    }

    @Test
    void failTestEditPostMultiple() {
        final String original = "this is my original text";
        final String newText = "";
        final JTextArea contentArea = new JTextArea();
        final ReadPostState state = createSamplePost();
        final User curUser = new User("Bob Marley", "bob1", "bobmarl@gmail.com",
                "bobby111222333");
        final JDialog dialog = new JDialog();
        final EditPostController editPostController =
                new EditPostController(new EditPostInputData(contentArea, state.getId(), curUser.getUsername(), state,
                        newText, dialog));
        editPostController.editPost();
        assertEquals(original, state.getContent());
    }
}
