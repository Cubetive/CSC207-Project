package interface_adapter.signup;

/**
 * The state for the Signup View Model.
 */
public class SignupState {
    private String fullName = "";
    private String username = "";
    private String email = "";
    private String password = "";
    private String repeatPassword = "";
    private String fullNameError;
    private String usernameError;
    private String emailError;
    private String passwordError;
    private String repeatPasswordError;

    /**
     * Copy constructor for SignupState.
     *
     * @param copy the SignupState to copy
     */
    public SignupState(SignupState copy) {
        this.fullName = copy.fullName;
        this.username = copy.username;
        this.email = copy.email;
        this.password = copy.password;
        this.repeatPassword = copy.repeatPassword;
        this.fullNameError = copy.fullNameError;
        this.usernameError = copy.usernameError;
        this.emailError = copy.emailError;
        this.passwordError = copy.passwordError;
        this.repeatPasswordError = copy.repeatPasswordError;
    }

    /**
     * Default constructor for SignupState.
     */
    public SignupState() {
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
     * Sets the full name.
     *
     * @param fullName the full name to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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
     * Sets the email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the repeat password.
     *
     * @return the repeat password
     */
    public String getRepeatPassword() {
        return repeatPassword;
    }

    /**
     * Sets the repeat password.
     *
     * @param repeatPassword the repeat password to set
     */
    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    /**
     * Gets the full name error message.
     *
     * @return the full name error, or null if no error
     */
    public String getFullNameError() {
        return fullNameError;
    }

    /**
     * Sets the full name error message.
     *
     * @param fullNameError the full name error to set
     */
    public void setFullNameError(String fullNameError) {
        this.fullNameError = fullNameError;
    }

    /**
     * Gets the username error message.
     *
     * @return the username error, or null if no error
     */
    public String getUsernameError() {
        return usernameError;
    }

    /**
     * Sets the username error message.
     *
     * @param usernameError the username error to set
     */
    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    /**
     * Gets the email error message.
     *
     * @return the email error, or null if no error
     */
    public String getEmailError() {
        return emailError;
    }

    /**
     * Sets the email error message.
     *
     * @param emailError the email error to set
     */
    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    /**
     * Gets the password error message.
     *
     * @return the password error, or null if no error
     */
    public String getPasswordError() {
        return passwordError;
    }

    /**
     * Sets the password error message.
     *
     * @param passwordError the password error to set
     */
    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    /**
     * Gets the repeat password error message.
     *
     * @return the repeat password error, or null if no error
     */
    public String getRepeatPasswordError() {
        return repeatPasswordError;
    }

    /**
     * Sets the repeat password error message.
     *
     * @param repeatPasswordError the repeat password error to set
     */
    public void setRepeatPasswordError(String repeatPasswordError) {
        this.repeatPasswordError = repeatPasswordError;
    }

    @Override
    public String toString() {
        return "SignupState{"
                + "fullName='" + fullName + '\''
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + '}';
    }
}
