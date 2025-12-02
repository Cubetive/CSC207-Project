package use_case.signup;

/**
 * Output Data for the Signup Use Case.
 */
public class SignupOutputData {
    private final String username;
    private final String fullName;
    private final boolean useCaseFailed;

    /**
     * Constructs a SignupOutputData.
     *
     * @param username the username
     * @param fullName the full name
     * @param useCaseFailed whether the use case failed
     */
    public SignupOutputData(String username, String fullName, boolean useCaseFailed) {
        this.username = username;
        this.fullName = fullName;
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Checks if the use case failed.
     *
     * @return true if failed, false otherwise
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
