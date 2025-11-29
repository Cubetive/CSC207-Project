package use_case.login;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {
    private final String username;
    private final String fullName;
    private final boolean useCaseFailed;

    public LoginOutputData(String username, String fullName, boolean useCaseFailed) {
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
