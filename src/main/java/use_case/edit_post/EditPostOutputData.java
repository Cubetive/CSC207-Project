package use_case.edit_post;

import interface_adapter.read_post.ReadPostState;

public class EditPostOutputData {
    private ReadPostState outputPost;

    public EditPostOutputData(ReadPostState outputPost) {
        this.outputPost = outputPost;
    }

    public ReadPostState getOutputPost() {
        return this.outputPost;
    }
}
