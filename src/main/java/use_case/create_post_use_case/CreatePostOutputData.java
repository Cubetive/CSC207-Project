package use_case.create_post_use_case;

import entities.OriginalPost;

import javax.crypto.SecretKey;
import java.util.Date;

public class CreatePostOutputData {
    private OriginalPost originalPost;

    public CreatePostOutputData(OriginalPost originalPost) {
        this.originalPost = originalPost;
    }

    public OriginalPost getOriginalPost() {
        return originalPost;
    }
}
