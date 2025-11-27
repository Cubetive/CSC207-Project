package use_case.edit_post;

import javax.swing.JDialog;

import entities.Post;
import interface_adapter.edit_post.EditPostPresenter;

public class EditPostInteractor implements EditPostInputBoundary {

    private EditPostOutputBoundary editPostOutputBoundary;

    @Override
    public void editPost(String username, Post postToEdit, String newContent, JDialog dialog) {
        if (postToEdit.getCreatorUsername().equals(username)) {
            postToEdit.editText(newContent);
        }

        EditPostOutputData searchPostOutputData = new EditPostOutputData(postToEdit);
        
        editPostOutputBoundary = new EditPostPresenter(searchPostOutputData);
        editPostOutputBoundary.prepareSuccessView(dialog);
    }
    
}
