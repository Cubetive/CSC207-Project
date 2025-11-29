package use_case.read_post;

/**
 * Input Boundary for the Read Post use case.
 */
public interface ReadPostInputBoundary {

    /**
     * Executes the read post use case.
     * @param inputData the input data containing the post identifier
     */
    void execute(ReadPostInputData inputData);
}
