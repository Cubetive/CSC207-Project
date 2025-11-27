package use_case.login;

/**
 * Input Boundary for the Login Use Case.
 */
public interface LoginInputBoundary {
    /**
     * Executes the login use case.
     * @param loginInputData the input data
     */
    void execute(LoginInputData loginInputData);

    /**
     * Switches to the Signup View.
     */
    void switchToSignupView();
}
