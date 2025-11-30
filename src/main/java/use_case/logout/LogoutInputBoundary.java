package use_case.logout;

public interface LogoutInputBoundary {
    /**
     * Executes log out use case. This clears
     * the data of the user for the current session.
     */
    void execute();
}
