package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String username = "";
    private String password = "";
    private String usernameError;
    private String passwordError;

    /**
     * Copy constructor for LoginState.
     *
     * @param copy the LoginState to copy
     */
    public LoginState(LoginState copy) {
        this.username = copy.username;
        this.password = copy.password;
        this.usernameError = copy.usernameError;
        this.passwordError = copy.passwordError;
    }

    /**
     * Default constructor for LoginState.
     */
    public LoginState() {
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

    @Override
    public String toString() {
        return "LoginState{"
                + "username='" + username + '\''
                + '}';
    }
}
