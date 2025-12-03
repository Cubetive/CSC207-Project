package interface_adapter.edit_post;

import javax.swing.JDialog;

import use_case.edit_post.EditPostOutputBoundary;
import use_case.edit_post.EditPostOutputData;

public class EditPostPresenter implements EditPostOutputBoundary {

    private EditPostOutputData editPostOutputData;

    public EditPostPresenter(EditPostOutputData editPostOutputData) {
        this.editPostOutputData = editPostOutputData;
    }

    @Override
    public void prepareSuccessView(JDialog dialog) {
        final EditPostViewModel editPostViewModel = new EditPostViewModel();
        editPostViewModel.updateView(editPostOutputData.getOutputPost().getContent(), dialog);
    }
}
