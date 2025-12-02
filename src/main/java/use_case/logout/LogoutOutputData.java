package use_case.logout;

/**
 * Output data for the Logout use case.
 */
public class LogoutOutputData {
    private final String username;

    /**
     * Constructs a LogoutOutputData.
     *
     * @param username the username of the logged out user
     */
    public LogoutOutputData(String username) {
        this.username = username;
    }

    /**
     * Gets the username of the logged out user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
