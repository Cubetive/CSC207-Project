package use_case.edit_post;

public class EditPostOutputData {
    private ReadPostState outputPost;

    public EditPostOutputData(ReadPostState outputPost) {
        this.outputPost = outputPost;
    }

    public ReadPostState getOutputPost() {
        return this.outputPost;
    }
}
