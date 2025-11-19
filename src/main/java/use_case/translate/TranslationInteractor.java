package use_case.translate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The Translation Interactor (Use Case), which contains the application-specific business logic.
 * It coordinates the flow of data and rules, using an ExecutorService to ensure time-consuming
 * Data Access (network calls) runs on a background thread, preventing the UI from freezing.
 */
public class TranslationInteractor {

    private final TranslationDataAccessInterface dataAccess;
    private final TranslationOutputBoundary outputBoundary;
    private final ExecutorService executor;

    public TranslationInteractor(
            TranslationDataAccessInterface dataAccess,
            TranslationOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        // Use a single thread pool for sequential, predictable execution of tasks
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Executes the translation use case logic. This method submits the actual work
     * to a background thread to prevent blocking the Swing Event Dispatch Thread (EDT).
     *
     * @param inputData The input data from the Controller.
     */
    public void execute(TranslationInputData inputData) {
        executor.submit(() -> {
            try {
                // 1. Validation Logic
                if (inputData.getSourceText().trim().isEmpty()) {
                    outputBoundary.presentFailure("Cannot translate empty text.");
                    return;
                }

                // This check now works because the interface was updated.
                if (!dataAccess.isLanguageSupported(inputData.getTargetLanguage())) {
                    outputBoundary.presentFailure("Target language is not supported.");
                    return;
                }

                // 2. Data Access Call (This is the long-running network operation)
                String translatedText = dataAccess.translate(
                        inputData.getSourceText(),
                        inputData.getTargetLanguage()
                );

                // 3. Prepare and Present Result
                if (translatedText.startsWith("ERROR:")) {
                    outputBoundary.presentFailure(translatedText);
                } else {
                    TranslationOutputData outputData = new TranslationOutputData(translatedText);
                    outputBoundary.presentSuccess(outputData);
                }
            } catch (Exception e) {
                // Catch unexpected runtime errors
                outputBoundary.presentFailure("An unexpected error occurred during translation: " + e.getMessage());
            }
        });
    }

    /**
     * Shuts down the background thread pool when the application closes.
     */
    public void shutdown() {
        executor.shutdown();
        try {
            // Wait a little while for existing tasks to terminate
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
}