import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ApplyFilters {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        Scanner input = new Scanner(System.in);
        System.out.println("Insert the name of the file path you would like to use.");
        String filePath = input.nextLine();

        System.out.println("\nChoose a filter to apply to the image:");
        System.out.format("|     Sequential      |     MultiThread      |                             Threadpool                             | %n");
        System.out.format("|---------------------|----------------------|       Executor       |     ForkJoinPool     |  CompletableFilters  |%n");
        System.out.format("| 1. Brighter         | 7. Brighter          | 13. Brighter         | 19. Brighter         | 25. Brighter         |%n");
        System.out.format("| 2. GrayScale        | 8. GrayScale         | 14. GrayScale        | 20. GrayScale        | 26. GrayScale        |%n");
        System.out.format("| 3. Swirl            | 9. Swirl             | 15. Swirl            | 21. Swirl            | 27. Swirl            |%n");
        System.out.format("| 4. Glass            | 10. Glass            | 16. Glass            | 22. Glass            | 28. Glass            |%n");
        System.out.format("| 5. Blur             | 11. Blur             | 17. Blur             | 23. Blur             | 29. Blur             |%n");
        System.out.format("| 6. Conditional Blur | 12. Conditional Blur | 18. Conditional Blur | 24. Conditional Blur | 30. Conditional Blur |%n\n");

        String filter = input.nextLine();
        input.close();

        applyFilter(filter, filePath);
    }

    public static void applyFilter(String filter, String filePath) throws IOException, InterruptedException, ExecutionException {
        Filters filters = new Filters(filePath);
        final int numThreads = 8;
        final int matrixSizeForBlur = 3;

        switch (filter) {
            case "1":
                long startTime1 = System.currentTimeMillis();

                filters.BrighterFilter("brighter.jpg", 128);

                long endTime1 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime1 - startTime1) + " milliseconds");

                break;
            case "2":
                long startTime2 = System.currentTimeMillis();

                filters.GrayScaleFilter("grayscale.jpg");

                long endTime2 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime2 - startTime2) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscale.jpg");
                break;
            case "4":
                long startTime4 = System.currentTimeMillis();

                filters.GlassFilter("glass.jpg");

                long endTime4 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime4 - startTime4) + " milliseconds");

                System.out.println("Glass filter applied to image on file glass.jpg");
                break;
            case "5":
                long startTime5 = System.currentTimeMillis();

                filters.BlurFilter("blur.jpg", matrixSizeForBlur);

                long endTime5 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime5 - startTime5) + " milliseconds");

                System.out.println("Blur filter applied to image on file blur.jpg");
                break;
            case "6":
                long startTime6 = System.currentTimeMillis();

                filters.ConditionalBlurFilter("conditionalBlur.jpg", matrixSizeForBlur);

                long endTime6 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime6 - startTime6) + " milliseconds");

                System.out.println("Conditional blur filter applied to image on file conditionalBlur.jpg");
                break;
            case "8":
                long startTime8 = System.currentTimeMillis();

                filters.GrayFilterMultiThread("grayscaleMultiThread.jpg", numThreads);

                long endTime8 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime8 - startTime8) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscaleMultiThread.jpg");
                break;
            case "10":
                long startTime10 = System.currentTimeMillis();

                filters.GlassFilterMultiThread("glassMultiThread.jpg", numThreads);

                long endTime10 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime10 - startTime10) + " milliseconds");

                System.out.println("Glass filter applied to image on file glassMultiThread.jpg");
                break;
            case "11":
                long startTime11 = System.currentTimeMillis();

                filters.BlurFilterMultiThread("blurMultithread.jpg", matrixSizeForBlur, numThreads);

                long endTime11 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime11 - startTime11) + " milliseconds");

                System.out.println("Blur filter applied to image on file blurMultithread.jpg");
                break;
            case "12":
                long startTime12 = System.currentTimeMillis();

                filters.ConditionalBlurFilterMultiThread("conditionalBlurMultiThread.jpg", numThreads, matrixSizeForBlur);

                long endTime12 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime12 - startTime12) + " milliseconds");

                System.out.println("Conditional blur filter applied to image on file conditionalBlurMultiThread.jpg");
                break;
            case "14":
                long startTime14 = System.currentTimeMillis();

                filters.GrayFilterThreadPool("grayscaleThreadPool.jpg", numThreads);

                long endTime14 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime14 - startTime14) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscaleThreadPool.jpg");
                break;
            case "16":
                long startTime16 = System.currentTimeMillis();

                filters.GlassFilterThreadPool("glassThreadPool.jpg", numThreads);

                long endTime16 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime16 - startTime16) + " milliseconds");

                System.out.println("\nGlass filter applied to image on file glassThreadPool.jpg");
                break;
            case "17":
                long startTime17 = System.currentTimeMillis();

                filters.BlurFilterThreadPool("blurThreadPool.jpg", matrixSizeForBlur, numThreads);

                long endTime17 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime17 - startTime17) + " milliseconds");

                System.out.println("\nBlur filter applied to image on file blurThreadPool.jpg");
                break;
            case "18":
                long startTime18 = System.currentTimeMillis();

                filters.ConditionalBlurFilterThreadPool("conditionalBlurThreadPool.jpg", numThreads, matrixSizeForBlur);

                long endTime18 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime18 - startTime18) + " milliseconds");

                System.out.println("\nConditional blur filter applied to image on file conditionalBlurThreadPool.jpg");
                break;

            case "20":
                long startTime20 = System.currentTimeMillis();

                filters.GrayFilterForkJoinPool("grayscaleForkJoinPool.jpg", numThreads);

                long endTime20 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime20 - startTime20) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscaleForkJoinPool.jpg");
                break;

            case "22":
                long startTime22 = System.currentTimeMillis();

                filters.GlassFilterForkJoinPool("glassForkJoinPool.jpg", numThreads);

                long endTime22 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime22 - startTime22) + " milliseconds");

                System.out.println("\nGlass filter applied to image on file glassForkJoinPool.jpg");
                break;

            case "26":
                long startTime26 = System.currentTimeMillis();

                filters.GrayFilterCompletableFuture("grayscaleCompletableFutures.jpg", numThreads);

                long endTime26 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime26 - startTime26) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscaleCompletableFutures.jpg");
                break;
            default:
                System.out.println("\nInvalid filter");
                break;
        }
    }
}