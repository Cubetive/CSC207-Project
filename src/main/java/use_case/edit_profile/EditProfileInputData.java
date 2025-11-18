package use_case.edit_profile;

/**
 * Input Data for the Edit Profile Use Case.
 */
public class EditProfileInputData {
    private final String currentUsername;
    private final String newUsername;
    private final String fullName;
    private final String bio;
    private final String profilePicture;
    private final String currentPassword;
    private final String newPassword;
    private final String repeatNewPassword;

    /**
     * Constructor for editing profile with optional password change.
     * @param currentUsername the current username of the user
     * @param newUsername the new username (can be same as current if not changing)
     * @param fullName the updated full name
     * @param bio the updated bio
     * @param profilePicture the updated profile picture URL/path
     * @param currentPassword the current password (for verification if changing password)
     * @param newPassword the new password (null or empty if not changing)
     * @param repeatNewPassword the repeated new password (null or empty if not changing)
     */
    public EditProfileInputData(String currentUsername, String newUsername, String fullName,
                               String bio, String profilePicture, String currentPassword,
                               String newPassword, String repeatNewPassword) {
        this.currentUsername = currentUsername;
        this.newUsername = newUsername;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.repeatNewPassword = repeatNewPassword;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    /**
     * Checks if the user is attempting to change their password.
     * @return true if new password is provided, false otherwise
     */
    public boolean isChangingPassword() {
        return newPassword != null && !newPassword.trim().isEmpty();
    }

    /**
     * Checks if the user is attempting to change their username.
     * @return true if new username differs from current username, false otherwise
     */
    public boolean isChangingUsername() {
        return !currentUsername.equals(newUsername);
    }
}
