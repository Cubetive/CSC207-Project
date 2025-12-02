package use_case.translate;

/**
 * The Input Boundary interface for the Translation Use Case.
 * This is the contract the Controller uses to communicate with the Interactor.
 * It enforces the Dependency Inversion Principle, allowing the Controller
 * to depend on an abstraction (this interface) rather than the concrete
 * TranslationInteractor implementation.
 */
public interface TranslationInputBoundary {
    /**
     * Triggers the execution of the translation process.
     * @param inputData The data bundle (text, target language, post ID) required
     *                  for the translation use case.
     */
    void execute(TranslationInputData inputData);
}
