package use_case.translate;

import use_case.read_post.ReadPostDataAccessInterface;
import entities.OriginalPost;
// FIX: Removed unnecessary ExecutorService imports as threading is now handled by the View's SwingWorker
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The Translation Interactor (Use Case), which contains the application-specific business logic.
 * It is responsible for orchestrating post fetching, language detection, and translation.
 * FIX: This Interactor is now synchronous. The View must call it from a non-EDT thread (like a SwingWorker).
 */
public class TranslationInteractor implements TranslationInputBoundary {

    private final ReadPostDataAccessInterface postDataAccessObject;
    private final TranslationDataAccessInterface translationDataAccessObject;
    private final TranslationOutputBoundary outputBoundary;
    // FIX: ExecutorService declaration removed.
    // private final ExecutorService executor;

    // FIX: Thread Pool size constants removed.
    // private static final int THREAD_POOL_SIZE = 5;

    public TranslationInteractor(
            ReadPostDataAccessInterface postDataAccessObject,
            TranslationDataAccessInterface translationDataAccessObject,
            TranslationOutputBoundary outputBoundary) {
        this.postDataAccessObject = postDataAccessObject;
        this.translationDataAccessObject = translationDataAccessObject;
        this.outputBoundary = outputBoundary;
        // FIX: Executor initialization removed.
        // this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    /**
     * Executes the translation use case logic SYNCHRONOUSLY.
     * The caller (View/Controller) is responsible for running this on a background thread.
     *
     * @param inputData The input data from the Controller.
     */
    @Override
    public void execute(TranslationInputData inputData) {
        // Use the DTO properties. Note that postId will be null for comments.
        final Long postId = inputData.getPostId();
        final String textContent = inputData.getTextContent();
        final String targetLanguageCode = inputData.getTargetLanguage();

        // FIX: Removed executor.submit(() -> { ... }); - logic now executes directly.
        try { // FIX: Start the try block directly
            // 1. VALIDATION:
            if (textContent == null || textContent.trim().isEmpty()) {
                outputBoundary.presentFailure("Cannot translate empty text.");
                return;
            }

            // Default status values
            boolean isFromCache = false;
            String translatedText = null;

            if (inputData.isPostTranslation()) {
                // --- POST TRANSLATION LOGIC (Caching Check) ---

                // Fetch the post entity to check for cached translation
                // NOTE: This assumes postId is not null here.
                OriginalPost post = postDataAccessObject.getPostById(postId);

                // NOTE: Assumes post.getTranslation(targetLanguageCode) exists and returns String
                String cachedTranslation = post.getTranslation(targetLanguageCode);

                if (cachedTranslation != null && !cachedTranslation.trim().isEmpty()) {
                    // Cache Hit
                    translatedText = cachedTranslation;
                    isFromCache = true;
                } else {
                    // Cache Miss - Perform API translation (This is the blocking DAO call)
                    translatedText = translationDataAccessObject.getTranslation(
                            textContent,
                            targetLanguageCode
                    );

                    // If successful, save to cache/database (simulated)
                    if (!translatedText.startsWith("ERROR:")) {
                        // FIX: Ensure save is called only if translation was successful and cache missed
                        translationDataAccessObject.saveTranslatedContent(postId, targetLanguageCode, translatedText);
                    }
                }
            } else {
                // --- COMMENT / RAW TEXT TRANSLATION LOGIC (No Caching) ---
                translatedText = translationDataAccessObject.getTranslation(
                        textContent,
                        targetLanguageCode
                );
            }

            // 2. PRESENT RESULT
            if (translatedText.startsWith("ERROR:")) {
                outputBoundary.presentFailure(translatedText);
            } else {
                // Success: Prepare output data
                TranslationOutputData outputData = new TranslationOutputData(
                        translatedText,
                        targetLanguageCode,
                        postId == null ? 0 : postId,
                        isFromCache
                );
                outputBoundary.presentSuccess(outputData);
            }

        } catch (Exception e) { // FIX: Close try block
            // Catch network, API, or unexpected runtime errors
            outputBoundary.presentFailure("A translation error occurred: " + e.getMessage());
        }
        // FIX: Removed closing executor bracket
    }

    /**
     * FIX: This method is now obsolete as there is no internal executor to shut down.
     */
    public void shutdown() {
        // FIX: Method body removed as Executor is gone.
    }
}