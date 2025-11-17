package entities;
import java.util.List;

public class ReplyPost extends Post {

    private List<ThreadPost> threads;

    public ReplyPost(String username, String content) {
        super(username, content);
    }

    public ThreadPost replyToPost(String text, String username) {
        // TODO
        return new ThreadPost(username, text);
    }
    
}