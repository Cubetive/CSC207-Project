package use_case.edit_post;

import javax.swing.JDialog;

import entities.Post;

public class EditPostInputData {

    private String username;
    private Post postToEdit;
    private String newContent;
    private JDialog dialog;

    public EditPostInputData(String username, Post postToEdit, String newContent, JDialog dialog) {
        this.username = username;
        this.postToEdit = postToEdit;
        this.newContent = newContent;
        this.dialog = dialog;
    }

    public String getUsername() {
        return this.username;
    }

    public String getNewContent() {
        return this.newContent;
    }

    public Post getPostToEdit() {
        return this.postToEdit;
    }

    public JDialog getDialog() {
        return this.dialog;
    }

}
