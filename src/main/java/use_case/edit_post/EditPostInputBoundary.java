package use_case.edit_post;

import javax.swing.JDialog;

import entities.Post;

/**
 * Input Boundary for Edit Post use case.
 */
public interface EditPostInputBoundary {
    /**
     * Executes the Edit Post use case.
     * @param username post attempted to be edited by username
     * @param postToEdit post to be editted
     * @param newContent the newly submitted content
     * @param dialog the dialog box
     */
    void editPost(String username, Post postToEdit, String newContent, JDialog dialog);
}
