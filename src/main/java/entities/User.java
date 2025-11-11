package entities;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class User {
    private String full_name;
    private String email;
    private String password;
    private String username;
    private Date dateJoined;
    private String profilePicture;
    private String bio;
    private List<OriginalPost> original_posts;
    private List<DirectMessage> messages;
    private List<ReplyPost> replies;

    public User(String full_name, String username, String email, String password){
        this.full_name = full_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateJoined = new Date();
        this.original_posts = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.replies = new ArrayList<>();
    }

    public void signUp() {
        // TODO: Implement signUp
    }

    public void login() {
        // TODO: Implement login
    }

    public void changePassword(String oldPassword, String newPassword) {
        // TODO: Implement changePassword
    }

    public void logout() {
        // TODO: Implement logout
    }

    public void editProfile(String full_name, String bio, String profilePicture) {
        // TODO: Implement editProfile
    }

    public void viewProfile() {
        // TODO: Implement viewProfile
    }

    public List<OriginalPost> browsePosts() {
        // TODO: Implement browsePosts
        return List.of();
    }

    public List<OriginalPost> searchPosts(String keyword) {
        // TODO: Implement searchPosts
        return List.of();
    }

    public String translateText(Post post, String output_lang) {
        // TODO: Implement translateText
        return output_lang;
    }

    public void upvotePost(Post post) {
        // TODO: Implement upvotePost
    }

    public void downvotePost(Post post) {
        // TODO: Implement downvotePoster
    }

    // Getters
    public String getFullName() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public List<OriginalPost> getOriginalPosts() {
        return original_posts;
    }

    public List<DirectMessage> getMessages() {
        return messages;
    }

    public List<ReplyPost> getReplies() {
        return replies;
    }
}

