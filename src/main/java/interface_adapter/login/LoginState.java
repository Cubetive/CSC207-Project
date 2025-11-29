package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String username = "";
    private String password = "";
    private String usernameError;
    private String passwordError;

    public LoginState(LoginState copy) {
        this.username = copy.username;
        this.password = copy.password;
        this.usernameError = copy.usernameError;
        this.passwordError = copy.passwordError;
    }

    public LoginState() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    @Override
    public String toString() {
        return "LoginState{" +
                "username='" + username + '\'' +
                '}';
    }
}
