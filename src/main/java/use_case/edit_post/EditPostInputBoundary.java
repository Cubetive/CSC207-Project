package use_case.edit_post;

import javax.swing.JDialog;

/**
 * Input Boundary for Edit Post use case.
 */
public interface EditPostInputBoundary {
    /**
     * Executes the Edit Post use case.
     * @param id the editing post's id
     * @param username post attempted to be edited by username
     * @param postToEdit post to be editted
     * @param contentNew the newly submitted content
     * @param dialog the dialog box
     */
    void editPost(int id, String username, ReadPostState postToEdit, String contentNew, JDialog dialog);
}
