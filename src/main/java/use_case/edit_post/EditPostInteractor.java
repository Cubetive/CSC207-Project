package use_case.edit_post;

import javax.swing.*;

import data_access.FilePostDataAccessObject;
import interface_adapter.edit_post.EditPostPresenter;
import interface_adapter.read_post.ReadPostState;

public class EditPostInteractor implements EditPostInputBoundary {

    private EditPostOutputBoundary editPostOutputBoundary;

    @Override
    public void editPost(JTextArea contentArea, long id, String username, ReadPostState postToEdit, String contentNew, JDialog dialog) {
        if (postToEdit.getUsername().equals(username) && contentNew.length() > 0) {
            postToEdit.setContent(contentNew);
            contentArea.setText(contentNew);

            FilePostDataAccessObject dao = new FilePostDataAccessObject("posts.json");
            dao.editPostContent(id, contentNew);
        }

        EditPostOutputData editPostOutputData = new EditPostOutputData(postToEdit);
        
        editPostOutputBoundary = new EditPostPresenter(editPostOutputData);
        editPostOutputBoundary.prepareSuccessView(dialog);
    }

}
