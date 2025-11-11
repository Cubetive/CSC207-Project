package entities;

public class Commentor extends User{
    public Commentor(String full_name, String username, String email, String password) {
        super(full_name, username, email, password);
    }

    public Post replyToPost(Post post, String text, String username){
        //TODO: implement replyToPost
        return null;

    }

    public void deletePost(ReplyPost post){
        //TODO: implement deletePost
    }

    public ReplyPost editPost(ReplyPost post, String text){
        //TODO: implement editPost
        return null;
    }

    public void referencePost(Post post){
        //TODO: implement referencePost
    }




}
