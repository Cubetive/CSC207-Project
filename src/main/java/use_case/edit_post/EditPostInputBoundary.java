package use_case.edit_post;

import interface_adapter.read_post.ReadPostState;

import javax.swing.*;

/**
 * Input Boundary for Edit Post use case.
 */
public interface EditPostInputBoundary {
    /**
     * Executes the Edit Post use case.
     * @param contentArea content area
     * @param id the editing post's id
     * @param username post attempted to be edited by username
     * @param postToEdit post to be editted
     * @param contentNew the newly submitted content
     * @param dialog the dialog box
     */
    void editPost(JTextArea contentArea, long id, String username, ReadPostState postToEdit, String contentNew, JDialog dialog);
}
