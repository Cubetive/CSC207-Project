package interface_adapter.translate;

import use_case.translate.TranslationInputBoundary;
import use_case.translate.TranslationInputData;

/**
 * Controller for the Translation Use Case.
 *
 * This class handles the request from the presentation layer (e.g., a button click in the UI),
 * formats the raw input data (text, language, ID) into a data transfer object (DTO),
 * and delegates the execution of the business logic to the Interactor via the Input Boundary.
 *
 * It resides in the Interface Adapters layer of the Clean Architecture.
 */
public class TranslationController {

    private final TranslationInputBoundary translationUseCase;

    /**
     * Constructs the TranslationController, injecting the Input Boundary dependency.
     * This adheres to the Dependency Inversion Principle (DIP).
     *
     * @param translationUseCase The Input Boundary interface (the 'port') that the
     * Controller uses to trigger the business logic.
     */
    public TranslationController(TranslationInputBoundary translationUseCase) {
        this.translationUseCase = translationUseCase;
    }

    /**
     * The main entry point for the translation action.
     *
     * @param originalText The text content to be translated.
     * @param targetLanguage The language code (e.g., "es", "fr") to translate into.
     * @param postId The unique identifier of the content being translated (if applicable).
     */
    public void execute(String originalText, String targetLanguage, String postId) {
        // 1. Package the raw input data into the TranslationInputData DTO.
        TranslationInputData inputData = new TranslationInputData(
                originalText,
                targetLanguage
        );

        // 2. Delegate the execution to the Use Case Interactor via the boundary interface.
        translationUseCase.execute(inputData);
    }
}