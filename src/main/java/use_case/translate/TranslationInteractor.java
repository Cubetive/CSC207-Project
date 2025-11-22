package use_case.translate;

import use_case.read_post.ReadPostDataAccessInterface;
import entities.OriginalPost;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The Translation Interactor (Use Case), which contains the application-specific business logic.
 * It is responsible for orchestrating post fetching, language detection, and translation.
 * It also implements a simple caching mechanism by checking if the required translation
 * is already stored within the OriginalPost entity.
 */
public class TranslationInteractor implements TranslationInputBoundary {

    private final ReadPostDataAccessInterface postDataAccessObject;
    private final TranslationDataAccessInterface translationDataAccessObject;
    private final TranslationOutputBoundary outputBoundary;
    private final ExecutorService executor;

    public TranslationInteractor(
            ReadPostDataAccessInterface postDataAccessObject,
            TranslationDataAccessInterface translationDataAccessObject,
            TranslationOutputBoundary outputBoundary) {
        this.postDataAccessObject = postDataAccessObject;
        this.translationDataAccessObject = translationDataAccessObject;
        this.outputBoundary = outputBoundary;
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Executes the translation use case logic on a background thread.
     *
     * @param inputData The input data from the Controller, containing the post ID and target language code.
     */
    @Override
    public void execute(TranslationInputData inputData) {
        final long postId = inputData.getPostId();
        final String textContent = inputData.getTextContent();
        final String targetLanguageCode = inputData.getTargetLanguage(); // Using inputData.getTargetLanguage()

        executor.submit(() -> {
            OriginalPost post = postDataAccessObject.getPostById(postId);

            try {
                // 1. VALIDATION & LANGUAGE DETECTION:
                if (textContent.trim().isEmpty()) {
                    outputBoundary.presentFailure("Cannot translate empty text.");
                    return;
                }

                // 2. CACHING CHECK: Check if the translation already exists in the post entity
                // NOTE: This assumes post.getTranslation() now exists as per the discussion logic.
                String cachedTranslation = post.getTranslation(targetLanguageCode);

                if (cachedTranslation != null && !cachedTranslation.trim().isEmpty()) {
                    // Translation is cached. We use the *detected* sourceLanguageCode for the output.
                    TranslationOutputData outputData = new TranslationOutputData(
                            cachedTranslation,
                            targetLanguageCode,
                            postId,
                            true // <-- CACHE HIT: Set to true
                    );
                    outputBoundary.presentSuccess(outputData);
                    return;
                }

                // 3. DATA ACCESS CALL: Cache Miss - Perform API translation
                String translatedText = translationDataAccessObject.getTranslation(
                        textContent,
                        targetLanguageCode
                );

                // 4. PRESENT RESULT
                if (translatedText.startsWith("ERROR:")) {
                    outputBoundary.presentFailure(translatedText);
                } else {
                    // Success: Prepare output data
                    // NOTE: This is where we would call post.addTranslation(targetLanguageCode, translatedText)
                    // and save the post back to the database in a real application.

                    TranslationOutputData outputData = new TranslationOutputData(
                            translatedText,
                            targetLanguageCode,
                            postId,
                            false // <-- CACHE MISS: Set to false
                    );
                    outputBoundary.presentSuccess(outputData);
                }

            } catch (Exception e) {
                // Catch network, API, or unexpected runtime errors
                outputBoundary.presentFailure("A translation error occurred: " + e.getMessage());
            }
        });
    }

    /**
     * Shuts down the background thread pool when the application closes.
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}