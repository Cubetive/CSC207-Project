package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String fullName;
    private String email;
    private String password;
    private String username;
    private final Date dateJoined;
    private String profilePicture;
    private String bio;
    private final List<OriginalPost> originalPosts;
    private final List<ReplyPost> replies;

    public User(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateJoined = new Date();
        this.originalPosts = new ArrayList<>();
        this.replies = new ArrayList<>();
    }

    /**
     * Change the user's password.
     * @param oldPassword The old password
     * @param newPassword The new password
     */
    public void changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
        }
    }

    /**
     * Saves the changes made to the user's profile.
     * @param newFullName The user's new full name
     * @param newBio The user's new bio
     * @param newProfilePicture The user's new profile picture path
     */
    public void editProfile(String newFullName, String newBio, String newProfilePicture) {
        if (newFullName != null && !newFullName.trim().isEmpty()) {
            this.fullName = newFullName;
        }
        if (newBio != null) {
            this.bio = newBio;
        }
        if (newProfilePicture != null) {
            this.profilePicture = newProfilePicture;
        }
    }

    // Getters
    public String getFullName() {
        return fullName;
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
        return originalPosts;
    }

    public List<ReplyPost> getReplies() {
        return replies;
    }

    // Setters for mutable fields
    public void setFullName(String full_name) {
        this.fullName = full_name;
    }

    public void setUsername(String username) {
        this.username = username;
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

    /**
     * Add user's original post.
     * @param post User's original post
     */
    public void addOriginalPost(OriginalPost post) {
        this.originalPosts.add(post);
    }

    /**
     * Add user's reply post.
     * @param reply User's reply post
     */
    public void addReply(ReplyPost reply) {
        this.replies.add(reply);
    }
}

