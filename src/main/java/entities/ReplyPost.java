package entities;
import java.util.List;

public class ReplyPost extends Post {

    private List<ThreadPost> threads;

    public ReplyPost(String username, String text) {
        super(username, text);
    }

    public ThreadPost replyToPost(String username, String text) {
        // TODO
        return new ThreadPost(username, text);
    }
    
}