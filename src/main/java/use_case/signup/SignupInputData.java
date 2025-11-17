package use_case.signup;

/**
 * Input Data for the Signup Use Case.
 */
public class SignupInputData {
    private final String fullName;
    private final String username;
    private final String email;
    private final String password;
    private final String repeatPassword;

    public SignupInputData(String fullName, String username, String email,
                          String password, String repeatPassword) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }
}
