package use_case.edit_post;

import javax.swing.JDialog;

public class EditPostInputData {

    private int id;
    private String username;
    private ReadPostState postToEdit;
    private String contentNew;
    private JDialog dialog;

    public EditPostInputData(int id, String username, ReadPostState postToEdit, String contentNew, JDialog dialog) {
        this.id = id;
        this.username = username;
        this.postToEdit = postToEdit;
        this.contentNew = contentNew;
        this.dialog = dialog;
    }

    public String getUsername() {
        return this.username;
    }

    public String getNewContent() {
        return this.contentNew;
    }

    public ReadPostState getPostToEdit() {
        return this.postToEdit;
    }

    public JDialog getDialog() {
        return this.dialog;
    }

    public int getID() {
        return this.id;
    }

}
