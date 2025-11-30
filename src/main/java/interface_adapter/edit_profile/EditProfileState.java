package interface_adapter.edit_profile;

/**
 * The state for the Edit Profile View Model.
 */
public class EditProfileState {
    private String currentUsername = "";
    private String newUsername = "";
    private String fullName = "";
    private String bio = "";
    private String profilePicture = "";
    private String currentPassword = "";
    private String newPassword = "";
    private String repeatNewPassword = "";
    
    private String usernameError;
    private String fullNameError;
    private String bioError;
    private String profilePictureError;
    private String passwordError;
    private String generalError;

    public EditProfileState(EditProfileState copy) {
        this.currentUsername = copy.currentUsername;
        this.newUsername = copy.newUsername;
        this.fullName = copy.fullName;
        this.bio = copy.bio;
        this.profilePicture = copy.profilePicture;
        this.currentPassword = copy.currentPassword;
        this.newPassword = copy.newPassword;
        this.repeatNewPassword = copy.repeatNewPassword;
        this.usernameError = copy.usernameError;
        this.fullNameError = copy.fullNameError;
        this.bioError = copy.bioError;
        this.profilePictureError = copy.profilePictureError;
        this.passwordError = copy.passwordError;
        this.generalError = copy.generalError;
    }

    public EditProfileState() {
    }

    // Getters
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

    public String getUsernameError() {
        return usernameError;
    }

    public String getFullNameError() {
        return fullNameError;
    }

    public String getBioError() {
        return bioError;
    }

    public String getProfilePictureError() {
        return profilePictureError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public String getGeneralError() {
        return generalError;
    }

    // Setters
    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public void setFullNameError(String fullNameError) {
        this.fullNameError = fullNameError;
    }

    public void setBioError(String bioError) {
        this.bioError = bioError;
    }

    public void setProfilePictureError(String profilePictureError) {
        this.profilePictureError = profilePictureError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public void setGeneralError(String generalError) {
        this.generalError = generalError;
    }

    @Override
    public String toString() {
        return "EditProfileState{" +
                "currentUsername='" + currentUsername + '\'' +
                ", newUsername='" + newUsername + '\'' +
                ", fullName='" + fullName + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
