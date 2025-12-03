package interface_adapter.edit_post;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class EditPostViewModel {

    /**
     * Closes the dialog box and shows up a pop-up, informing the user that the post is updated.
     * @param updatedText The new content of the post
     * @param dialog The dialog box shown
     */
    public void updateView(String updatedText, JDialog dialog) {
        JOptionPane.showMessageDialog(dialog, "Post updated!\n\nNew Text:\n" + updatedText);
        dialog.dispose();
    }
    
}
