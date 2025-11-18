package use_case.edit_profile;

/**
 * Output Data for the Edit Profile Use Case.
 */
public class EditProfileOutputData {
    private final String username;
    private final String fullName;
    private final String bio;
    private final String profilePicture;
    private final boolean useCaseFailed;

    public EditProfileOutputData(String username, String fullName, String bio,
                                String profilePicture, boolean useCaseFailed) {
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
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

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
