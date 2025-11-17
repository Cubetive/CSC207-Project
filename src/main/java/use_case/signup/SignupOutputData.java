package use_case.signup;

/**
 * Output Data for the Signup Use Case.
 */
public class SignupOutputData {
    private final String username;
    private final String fullName;
    private final boolean useCaseFailed;

    public SignupOutputData(String username, String fullName, boolean useCaseFailed) {
        this.username = username;
        this.fullName = fullName;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
