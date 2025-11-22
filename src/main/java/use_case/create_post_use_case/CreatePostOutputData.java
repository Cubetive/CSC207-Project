package use_case.create_post_use_case;

import javax.crypto.SecretKey;
import java.util.Date;

public class CreatePostOutputData {
    private String title;
    private String content;
    private Date creation_date;
    private int[] votes;

    public CreatePostOutputData(String title, String content, Date creation_date, int[] votes) {
        this.content=content;
        this.title=title;
        this.creation_date=creation_date;
        this.votes=votes;
    }

}
