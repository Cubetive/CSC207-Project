package use_case.edit_post;

import entities.User;
import interface_adapter.edit_post.EditPostController;
import interface_adapter.read_post.ReadPostState;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class EditPostInteractorTest {

    ReadPostState createSamplePost() {
        ReadPostState state = new ReadPostState();
        state.setUsername("bob123");
        state.setTitle("This is the title");
        state.setContent("this is my original text");
        state.setId(1);
        return state;
    }

    @Test
    void successTestEditPost() {
        String newText = "this is my new text";
        JTextArea contentArea = new JTextArea();
        ReadPostState state = createSamplePost();
        User cur_user = new User("Bob Marley", "bob123", "bobmarl@gmail.com", "bobby111222333");
        JDialog dialog = new JDialog();
        EditPostController editPostController =
                new EditPostController(new EditPostInputData(contentArea, state.getId(), cur_user.getUsername(), state, newText, dialog));
        editPostController.editPost();
        assertEquals(newText, state.getContent());
    }

    @Test
    void failTestEditPostLength() {
        String original = "this is my original text";
        String newText = "";
        JTextArea contentArea = new JTextArea();
        ReadPostState state = createSamplePost();
        User cur_user = new User("Bob Marley", "bob123", "bobmarl@gmail.com", "bobby111222333");
        JDialog dialog = new JDialog();
        EditPostController editPostController =
                new EditPostController(new EditPostInputData(contentArea, state.getId(), cur_user.getUsername(), state, newText, dialog));
        editPostController.editPost();
        assertEquals(original, state.getContent());
    }

    @Test
    void failTestEditPostUsername() {
        String original = "this is my original text";
        String newText = "this is my new text";
        JTextArea contentArea = new JTextArea();
        ReadPostState state = createSamplePost();
        User cur_user = new User("Bob Marley", "bob1", "bobmarl@gmail.com", "bobby111222333");
        JDialog dialog = new JDialog();
        EditPostController editPostController =
                new EditPostController(new EditPostInputData(contentArea, state.getId(), cur_user.getUsername(), state, newText, dialog));
        editPostController.editPost();
        assertEquals(original, state.getContent());
    }

    @Test
    void failTestEditPostMultiple() {
        String original = "this is my original text";
        String newText = "";
        JTextArea contentArea = new JTextArea();
        ReadPostState state = createSamplePost();
        User cur_user = new User("Bob Marley", "bob1", "bobmarl@gmail.com", "bobby111222333");
        JDialog dialog = new JDialog();
        EditPostController editPostController =
                new EditPostController(new EditPostInputData(contentArea, state.getId(), cur_user.getUsername(), state, newText, dialog));
        editPostController.editPost();
        assertEquals(original, state.getContent());
    }
}
