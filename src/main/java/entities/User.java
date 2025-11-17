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
    private List<ReplyPost> replies;

    public User(String full_name, String username, String email, String password){
        this.full_name = full_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateJoined = new Date();
        this.original_posts = new ArrayList<>();
        this.replies = new ArrayList<>();
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
        List<OriginalPost> search_list = new ArrayList<OriginalPost>();
        for(int i = 0; i < this.original_posts.size(); i++) {
            if (this.original_posts.get(i).getTitle().contains(keyword)) {
                search_list.add(this.original_posts.get(i));
            }
        }
        return search_list;
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

    public String getPassword() {
        return password;
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

    public List<ReplyPost> getReplies() {
        return replies;
    }

    // Setters for mutable fields
    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void addOriginalPost(OriginalPost post) {
        this.original_posts.add(post);
    }

    public void addReply(ReplyPost reply) {
        this.replies.add(reply);
    }
}

