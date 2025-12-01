package use_case.translate;

import entities.OriginalPost;
import entities.Post;
import use_case.read_post.ReadPostDataAccessInterface;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

class TranslationInteractorTest {

    @Test
    void successCommentTranslationTest() {
        // 1. Arrange: Input data for a comment (Post ID is null)
        TranslationInputData inputData = new TranslationInputData("fr", "Hello world");

        // Fake Post DAO (Not used for comments, can return null)
        ReadPostDataAccessInterface postDAO = new ReadPostDataAccessInterface() {
            @Override
            public OriginalPost getPostById(long id) {
                return null;
            }
        };

        // Fake Translation DAO (Simulates the API)
        TranslationDataAccessInterface translationDAO = new TranslationDataAccessInterface() {
            @Override
            public String getTranslation(String text, String targetLanguage) {
                assertEquals("Hello world", text);
                assertEquals("fr", targetLanguage);
                return "Bonjour le monde"; // Return fake translated text
            }

            @Override
            public void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText) {
                fail("Should not save comment translations to post cache.");
            }
        };

        // Fake Presenter (Verifies success)
        TranslationOutputBoundary successPresenter = new TranslationOutputBoundary() {
            @Override
            public void presentSuccess(TranslationOutputData outputData) {
                assertEquals("Bonjour le monde", outputData.getTranslatedText());
                assertEquals("fr", outputData.getTargetLanguage());
            }

            @Override
            public void presentFailure(String errorMessage) {
                fail("Translation should not fail for valid input.");
            }
        };

        // 2. Act
        TranslationInputBoundary interactor = new TranslationInteractor(postDAO, translationDAO, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void successPostTranslationTest() {
        // 1. Arrange: Input data for a Main Post (Has ID)
        // Note: We pass the content too, as per your controller logic
        TranslationInputData inputData = new TranslationInputData("es", 1L, "Hello");

        // Fake Post DAO (Returns a valid post)
        ReadPostDataAccessInterface postDAO = new ReadPostDataAccessInterface() {
            @Override
            public OriginalPost getPostById(long id) {
                // Return a dummy post
                return new OriginalPost(1L, "Title", "Hello", "User", new Date(), 0, 0);
            }
        };

        // Fake Translation DAO
        TranslationDataAccessInterface translationDAO = new TranslationDataAccessInterface() {
            @Override
            public String getTranslation(String text, String targetLanguage) {
                return "Hola"; // Fake Spanish translation
            }

            @Override
            public void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText) {
                // Verify caching is attempted
                assertEquals(1L, postId);
                assertEquals("es", targetLanguageCode);
                assertEquals("Hola", translatedText);
            }
        };

        // Fake Presenter
        TranslationOutputBoundary successPresenter = new TranslationOutputBoundary() {
            @Override
            public void presentSuccess(TranslationOutputData outputData) {
                assertEquals("Hola", outputData.getTranslatedText());
                assertEquals(1L, outputData.getPostId());
            }

            @Override
            public void presentFailure(String errorMessage) {
                fail("Translation should not fail.");
            }
        };

        // 2. Act
        TranslationInputBoundary interactor = new TranslationInteractor(postDAO, translationDAO, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyTextTest() {
        // 1. Arrange: Empty input text
        TranslationInputData inputData = new TranslationInputData("es", "");

        // DAOs can be null or minimal since we expect fail before calling them
        ReadPostDataAccessInterface postDAO = new ReadPostDataAccessInterface() {
            @Override
            public OriginalPost getPostById(long id) {
                return null;
            }
        };
        TranslationDataAccessInterface translationDAO = new TranslationDataAccessInterface() {
            @Override
            public String getTranslation(String text, String targetLanguage) {
                return "";
            }
            @Override
            public void saveTranslatedContent(long postId, String targetLanguageCode, String translatedText) {}
        };

        // Fake Presenter (Expects Failure)
        TranslationOutputBoundary failurePresenter = new TranslationOutputBoundary() {
            @Override
            public void presentSuccess(TranslationOutputData outputData) {
                fail("Should not succeed with empty text.");
            }

            @Override
            public void presentFailure(String errorMessage) {
                // Verify error message is about empty text
                assertNotNull(errorMessage);
                assertTrue(errorMessage.contains("empty"));
            }
        };

        // 2. Act
        TranslationInputBoundary interactor = new TranslationInteractor(postDAO, translationDAO, failurePresenter);
        interactor.execute(inputData);
    }
}