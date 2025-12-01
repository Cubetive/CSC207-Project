package use_case.edit_post;

import javax.swing.JDialog;

/**
 * Output Boundary for Edit Post use case.
 */
public interface EditPostOutputBoundary {

    /**
     * Prepares the success view for the Edit Post use case.
     * @param dialog the dialog box
     */
    void prepareSuccessView(JDialog dialog);
}
