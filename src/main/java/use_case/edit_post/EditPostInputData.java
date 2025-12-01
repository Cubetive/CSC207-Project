package use_case.edit_post;

import interface_adapter.read_post.ReadPostState;

import javax.swing.*;

public class EditPostInputData {

    private JTextArea contentArea;
    private long id;
    private String username;
    private ReadPostState postToEdit;
    private String contentNew;
    private JDialog dialog;

    public EditPostInputData(JTextArea contentArea, long id, String username, ReadPostState postToEdit, String contentNew, JDialog dialog) {
        this.contentArea = contentArea;
        this.id = id;
        this.username = username;
        this.postToEdit = postToEdit;
        this.contentNew = contentNew;
        this.dialog = dialog;
    }

    public JTextArea getContentArea() {return this.contentArea;}

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

    public long getID() {
        return this.id;
    }

}
