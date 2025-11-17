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

    public SignupState() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getFullNameError() {
        return fullNameError;
    }

    public void setFullNameError(String fullNameError) {
        this.fullNameError = fullNameError;
    }

    public String getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getRepeatPasswordError() {
        return repeatPasswordError;
    }

    public void setRepeatPasswordError(String repeatPasswordError) {
        this.repeatPasswordError = repeatPasswordError;
    }

    @Override
    public String toString() {
        return "SignupState{" +
                "fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
