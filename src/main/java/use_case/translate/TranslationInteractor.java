package use_case.translate;

import use_case.read_post.ReadPostDataAccessInterface;
import entities.OriginalPost;
import java.util.concurrent.TimeUnit;

/**
 * The Translation Interactor (Use Case).
 * FIX: This Interactor is now fully synchronous and includes a diagnostic log immediately before the DAO call.
 */
public class TranslationInteractor implements TranslationInputBoundary {

    private final ReadPostDataAccessInterface postDataAccessObject;
    private final TranslationDataAccessInterface translationDataAccessObject;
    private final TranslationOutputBoundary outputBoundary;

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

        try {
            if (textContent == null || textContent.trim().isEmpty()) {
                System.out.println("DEBUG: Interactor decided FAILURE.");
                outputBoundary.presentFailure("Cannot translate empty text.");
                return;
            }

            boolean isFromCache = false;
            String translatedText = null;

            System.out.println("DEBUG: Interactor received text: [" + translatedText + "]");

            if (inputData.isPostTranslation()) {

                // Use the generic 'Post' type since the interface now returns 'Post'
                entities.Post post = postDataAccessObject.getPostById(postId);

                if (post == null) {
                    outputBoundary.presentFailure("ERROR: Post entity not found for ID " + postId + ". Please check if your JSON parser is assigning a unique ID to the post entity.");
                    return;
                }

                String cachedTranslation = "";

                if (cachedTranslation != null && !cachedTranslation.trim().isEmpty()) {
                    // Cache Hit
                    translatedText = cachedTranslation;
                    isFromCache = true;
                } else {
                    assert translationDataAccessObject != null;
                    translatedText = translationDataAccessObject.getTranslation(
                            textContent,
                            targetLanguageCode
                    );

                    // If successful, save to cache/database (simulated)
                    if (!translatedText.startsWith("ERROR:")) {
                        translationDataAccessObject.saveTranslatedContent(postId, targetLanguageCode, translatedText);
                    }
                }
            } else {
                System.out.println("DEBUG: Interactor decided SUCCESS.");
                translatedText = translationDataAccessObject.getTranslation(
                        textContent,
                        targetLanguageCode
                );
                if (translatedText == null) {
                    translatedText = "ERROR: Translation result returned null from DAO.";
                }
            }
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
}