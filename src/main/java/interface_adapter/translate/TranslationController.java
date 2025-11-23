package interface_adapter.translate;

import use_case.translate.TranslationInputBoundary;
import use_case.translate.TranslationInputData;
import use_case.translate.TranslationInteractor;

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

    private final TranslationInputBoundary translationInputBoundary;

    /**
     * Constructs the TranslationController, injecting the Input Boundary dependency.
     * This adheres to the Dependency Inversion Principle (DIP).
     *
     * @param translationInputBoundary The Input Boundary interface (the 'port') that the
     * Controller uses to trigger the business logic.
     */
    public TranslationController(TranslationInputBoundary translationInputBoundary) {
        this.translationInputBoundary = translationInputBoundary;
    }

    /**
     * The main entry point for the translation action for a main post.
     * @param postId The id of the post.
     * @param targetLanguage The language code (e.g., "es", "fr") to translate into.
     */
// 💡 Add the textContent parameter here
    public void execute(long postId, String textContent, String targetLanguage) {
        // 1. Package the raw input data into the TranslationInputData DTO (Post ID and textContent are needed).
        TranslationInputData inputData = new TranslationInputData(
                targetLanguage,
                postId, // 💡 Pass the postId
                textContent // 💡 Pass the text content
        );

        // 2. Delegate the execution to the Use Case Interactor via the boundary interface.
        translationInputBoundary.execute(inputData);
    }

    /**
     * Overload for the translation action for raw text (e.g., a comment or reply).
     * This method is used when the content is not a full post stored in the database.
     * @param rawText The raw text content to be translated.
     * @param targetLanguage The language code (e.g., "es", "fr") to translate into.
     */
    public void execute(String rawText, String targetLanguage) {
        // 1. Package the raw input data into the TranslationInputData DTO (using the raw text constructor).
        // The Interactor will know this is a raw text translation because postId will be null.
        TranslationInputData inputData = new TranslationInputData(
                targetLanguage,
                rawText
        );

        // 2. Delegate the execution to the Use Case Interactor via the boundary interface.
        translationInputBoundary.execute(inputData);
    }
}