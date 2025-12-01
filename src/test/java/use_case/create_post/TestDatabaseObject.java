package use_case.create_post;

import entities.OriginalPost;

public interface TestDatabaseObject {
    OriginalPost save(OriginalPost post, String blurb);
}
