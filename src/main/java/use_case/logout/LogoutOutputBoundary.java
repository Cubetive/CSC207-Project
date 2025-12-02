package use_case.logout;

/**
 * Output boundary for the Logout use case.
 */
public interface LogoutOutputBoundary {
    /**
     * Prepares the success view for the Logout Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(LogoutOutputData outputData);
}
