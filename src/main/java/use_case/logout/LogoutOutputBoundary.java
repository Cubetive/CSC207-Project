package use_case.logout;

import use_case.login.LoginOutputData;

public interface LogoutOutputBoundary {
    /**
     * Prepares the success view for the Logout Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(LogoutOutputData outputData);
}
