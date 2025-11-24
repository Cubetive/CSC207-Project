package use_case.translate;

import use_case.read_post.ReadPostDataAccessInterface;
import entities.OriginalPost;
import java.util.concurrent.TimeUnit;
// FIX: Added java.lang.System import is implicit, no explicit import needed.

/**
 * The Translation Interactor (Use Case).
 * FIX: This Interactor is now fully synchronous and includes a diagnostic log immediately before the DAO call.
 */
public class TranslationInteractor implements TranslationInputBoundary {

    private final ReadPostDataAccessInterface postDataAccessObject;
    private final TranslationDataAccessInterface translationDataAccessObject;
    private final TranslationOutputBoundary outputBoundary;

    // FIX: Removed ExecutorService declaration and initialization.

    public TranslationInteractor(
            ReadPostDataAccessInterface postDataAccessObject,
            TranslationDataAccessInterface translationDataAccessObject,
            TranslationOutputBoundary outputBoundary) {
        this.postDataAccessObject = postDataAccessObject;
        this.translationDataAccessObject = translationDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Executes the translation use case logic SYNCHRONOUSLY.
     */
    @Override
    public void execute(TranslationInputData inputData) {
        final Long postId = inputData.getPostId();
        final String textContent = inputData.getTextContent();
        final String targetLanguageCode = inputData.getTargetLanguage();

        // Check key dependencies needed for the main post logic
        System.out.println("INTERACTOR DEBUG: Main Post Interactor entered. Post ID: " + postId);
        System.out.println("INTERACTOR DEBUG: Checking PostDAO is null: " + (postDataAccessObject == null));
        System.out.println("INTERACTOR DEBUG: Checking TranslationDAO is null: " + (translationDataAccessObject == null));

        try {
            if (textContent == null || textContent.trim().isEmpty()) {
                outputBoundary.presentFailure("Cannot translate empty text.");
                return;
            }

            boolean isFromCache = false;
            String translatedText = null;

            if (inputData.isPostTranslation()) {
                // --- POST TRANSLATION LOGIC (Caching Check) ---

                OriginalPost post = postDataAccessObject.getPostById(postId);

                // FIX: CRITICAL NULL CHECK to prevent NullPointerException that causes the hang
                if (post == null) {
                    outputBoundary.presentFailure("ERROR: Post entity not found for ID " + postId + ". Please check if your JSON parser is assigning a unique ID to the post entity.");
                    return;
                }

                // NOTE: Assumes post.getTranslation(targetLanguageCode) exists and returns String
                String cachedTranslation = post.getTranslation(targetLanguageCode);

                if (cachedTranslation != null && !cachedTranslation.trim().isEmpty()) {
                    // Cache Hit
                    translatedText = cachedTranslation;
                    isFromCache = true;
                } else {
                    // Cache Miss - Perform API translation (This is the blocking DAO call)
                    // FIX: Diagnostic log to confirm thread reached the DAO boundary
                    System.out.println("DIAGNOSTIC: Interactor is calling DAO for translation now...");

                    assert translationDataAccessObject != null;
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
                // --- COMMENT / RAW TEXT TRANSLATION LOGIC ---
                // FIX: Diagnostic log for comment path
                System.out.println("DIAGNOSTIC: Interactor is calling DAO for comment translation now...");
                translatedText = translationDataAccessObject.getTranslation(
                        textContent,
                        targetLanguageCode
                );
                // 🔥 CRITICAL FIX: Ensure translatedText is not null here too
                if (translatedText == null) {
                    translatedText = "ERROR: Translation result returned null from DAO.";
                }
            }

            // 2. PRESENT RESULT
            if (translatedText.startsWith("ERROR:")) {
                outputBoundary.presentFailure(translatedText);
            } else {
                TranslationOutputData outputData = new TranslationOutputData(
                        translatedText,
                        targetLanguageCode,
                        postId == null ? 0 : postId,
                        isFromCache
                );
                outputBoundary.presentSuccess(outputData);
            }

        } catch (Exception e) {
            outputBoundary.presentFailure("A translation error occurred: " + e.getMessage());
        }
    }

    /**
     * FIX: Method body removed as Executor is gone.
     */
    public void shutdown() {
    }
}