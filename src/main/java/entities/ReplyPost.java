package entities;
import java.util.List;

public class ReplyPost extends Post {

    private List<ThreadPost> threads;

    public ReplyPost(String content, String username) {
        // TODO
    }

    public ThreadPost replyToPost(String text, String username) {
        // TODO
        return new ThreadPost(text, username);
    }
    
}