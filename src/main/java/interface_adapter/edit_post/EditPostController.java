package interface_adapter.edit_post;

import use_case.edit_post.EditPostInputBoundary;
import use_case.edit_post.EditPostInputData;
import use_case.edit_post.EditPostInteractor;

public class EditPostController {

    private EditPostInputBoundary editPostInputBoundary;
    private EditPostInputData editPostInputData;

    public EditPostController(EditPostInputData editPostInputData) {
        this.editPostInputData = editPostInputData;
    }

    /**
     * Executes the Edit Post Use Case.
     */
    public void editPost() {
        editPostInputBoundary = new EditPostInteractor();
        editPostInputBoundary.editPost(editPostInputData.getContentArea(), editPostInputData.getID(), editPostInputData.getUsername(), editPostInputData.getPostToEdit(), editPostInputData.getNewContent(), editPostInputData.getDialog());
    }
}
