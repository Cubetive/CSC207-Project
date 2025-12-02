package use_case.translate;

import entities.Post;
import use_case.read_post.ReadPostDataAccessInterface;

/**
 * The Translation Interactor (Use Case).
 * This Interactor is fully synchronous and includes diagnostic logging.
 */
public class TranslationInteractor implements TranslationInputBoundary {

    private final ReadPostDataAccessInterface postDataAccessObject;
    private final TranslationDataAccessInterface translationDataAccessObject;
    private final TranslationOutputBoundary outputBoundary;

    /**
     * Constructs a TranslationInteractor.
     *
     * @param postDataAccessObject the data access object for posts
     * @param translationDataAccessObject the data access object for translations
     * @param outputBoundary the output boundary for presenting results
     */
    public TranslationInteractor(
            ReadPostDataAccessInterface postDataAccessObject,
            TranslationDataAccessInterface translationDataAccessObject,
            TranslationOutputBoundary outputBoundary) {
        this.postDataAccessObject = postDataAccessObject;
        this.translationDataAccessObject = translationDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Executes the translation use case logic synchronously.
     *
     * @param inputData the input data containing text and target language
     */
    @Override
    public void execute(TranslationInputData inputData) {
        final Long postId = inputData.getPostId();
        final String textContent = inputData.getTextContent();
        final String targetLanguageCode = inputData.getTargetLanguage();

        if (textContent == null || textContent.trim().isEmpty()) {
            System.out.println("DEBUG: Interactor decided FAILURE.");
            outputBoundary.presentFailure("Cannot translate empty text.");
            return;
        }

        final boolean isFromCache = false;
        String translatedText = null;

        System.out.println("DEBUG: Interactor received text: [" + translatedText + "]");

        if (inputData.isPostTranslation()) {
            translatedText = handlePostTranslation(postId, textContent, targetLanguageCode);
            if (translatedText == null) {
                return;
            }
        }
        else {
            System.out.println("DEBUG: Interactor decided SUCCESS.");
            translatedText = translationDataAccessObject.getTranslation(textContent, targetLanguageCode);
            if (translatedText == null) {
                translatedText = "ERROR: Translation result returned null from DAO.";
            }
        }

        if (translatedText.startsWith("ERROR:")) {
            outputBoundary.presentFailure(translatedText);
        }
        else {
            final TranslationOutputData outputData = new TranslationOutputData(
                    translatedText,
                    targetLanguageCode,
                    postId == null ? 0 : postId,
                    isFromCache
            );
            outputBoundary.presentSuccess(outputData);
        }
    }

    private String handlePostTranslation(Long postId, String textContent, String targetLanguageCode) {
        final Post post = postDataAccessObject.getPostById(postId);

        if (post == null) {
            outputBoundary.presentFailure("ERROR: Post entity not found for ID " + postId
                    + ". Please check if your JSON parser is assigning a unique ID to the post entity.");
            return null;
        }

        final String cachedTranslation = "";
        final String translatedText;

        if (cachedTranslation != null && !cachedTranslation.trim().isEmpty()) {
            translatedText = cachedTranslation;
        }
        else {
            translatedText = translationDataAccessObject.getTranslation(textContent, targetLanguageCode);

            if (!translatedText.startsWith("ERROR:")) {
                translationDataAccessObject.saveTranslatedContent(postId, targetLanguageCode, translatedText);
            }
        }

        return translatedText;
    }
}
