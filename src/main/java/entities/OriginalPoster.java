package entities;

public class OriginalPoster extends User{

    public OriginalPoster(String full_name, String username, String email, String password) {
        super(full_name, username, email, password);
    }

    public OriginalPoster createPost(String title, String username){
        //TODO: implement OriginalPost
        return null;
    }

    public void deletePost(OriginalPoster post){
        //TODO: implement deletePost
    }

    public OriginalPost editPost(OriginalPost post, String text){
        post.editText(text);
        return post;
    }

    public void referencePost(Post post){
        //TODO: implement referencePost
    }

}
