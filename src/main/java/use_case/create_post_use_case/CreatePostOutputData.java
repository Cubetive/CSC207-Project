package use_case.create_post_use_case;

import entities.OriginalPost;

public class CreatePostOutputData {
    /**
     * The post successfully created.
     */
    private OriginalPost originalPost;

    /**
     * Construction of Output data for presenter.
     * @param originalPost post object to be sent to display.
     */
    public CreatePostOutputData(OriginalPost originalPost) {
        this.originalPost = originalPost;
    }

    /**
     * Getter for stored data.
     * @return the post created.
     */
    public OriginalPost getOriginalPost() {
        return originalPost;
    }
}
