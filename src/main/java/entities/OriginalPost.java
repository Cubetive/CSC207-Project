package entities;

public class OriginalPost extends Post {
    private final String title;

    public OriginalPost(String title, String content, String username) {
        super(username, content);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}