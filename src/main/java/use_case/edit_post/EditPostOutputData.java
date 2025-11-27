package use_case.edit_post;

import entities.Post;

public class EditPostOutputData {
    private Post outputPost;

    public EditPostOutputData(Post outputPost) {
        this.outputPost = outputPost;
    }

    public Post getOutputPost() {
        return this.outputPost;
    }
}
