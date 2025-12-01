package interface_adapter.edit_post;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class EditPostViewModel {

     public void updateView(String updatedText, JDialog dialog) {
                    JOptionPane.showMessageDialog(dialog,
                    "Post updated!\n\nNew Text:\n" + updatedText);

            dialog.dispose();
    }
    
}
