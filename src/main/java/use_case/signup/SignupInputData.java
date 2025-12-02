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

    /**
     * Constructs a SignupInputData.
     *
     * @param fullName the user's full name
     * @param username the desired username
     * @param email the user's email address
     * @param password the password
     * @param repeatPassword the repeated password for confirmation
     */
    public SignupInputData(String fullName, String username, String email,
                          String password, String repeatPassword) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
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
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the repeated password.
     *
     * @return the repeated password
     */
    public String getRepeatPassword() {
        return repeatPassword;
    }
}
