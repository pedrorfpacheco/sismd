import java.io.IOException;
import java.util.Scanner;

public class ApplyFilters {

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner input = new Scanner(System.in);
        System.out.println("Insert the name of the file path you would like to use.");
        String filePath = input.nextLine();

        System.out.format("|     Sequential      |     MultiThread      |                             Threadpool                             | %n");
        System.out.format("|---------------------|----------------------|       Executor       |     ForkJoinPool     |  CompletableFilters  |%n");
        System.out.format("| 1. Brighter         | 7. Brighter          | 13. Brighter         | 19. Brighter         | 25. Brighter         |%n");
        System.out.format("| 2. GrayScale        | 8. GrayScale         | 14. GrayScale        | 20. GrayScale        | 26. GrayScale        |%n");
        System.out.format("| 3. Swirl            | 9. Swirl             | 15. Swirl            | 21. Swirl            | 27. Swirl            |%n");
        System.out.format("| 4. Glass            | 10. Glass            | 16. Glass            | 22. Glass            | 28. Glass            |%n");
        System.out.format("| 5. Blur             | 11. Blur             | 17. Blur             | 23. Blur             | 29. Blur             |%n");
        System.out.format("| 6. Conditional Blur | 12. Conditional Blur | 18. Conditional Blur | 24. Conditional Blur | 30. Conditional Blur |%n");

        String filter = input.nextLine();
        input.close();

        applyFilter(filter, filePath);
    }

    public static void applyFilter(String filter, String filePath) throws IOException, InterruptedException {
        Filters filters = new Filters(filePath);
        final int numThreads = 8;
        final int matrixSizeForBlur=3;

        switch (filter) {
            case "1":
                measureExecutionTime(() -> {
                    try {
                        filters.BrighterFilter("brighter.jpg", 128);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                break;
            case "2":
                measureExecutionTime(() -> {
                    filters.GrayScaleFilter("grayscale.jpg");
                });

                System.out.println("\nGrayscale filter applied to image on file grayscale.jpg");
                break;
            case "4":
                measureExecutionTime(() -> {
                    filters.GlassFilter("glass.jpg");
                });
                break;
            case "5":
                measureExecutionTime(() -> {
                    filters.BlurFilter("blur.jpg",matrixSizeForBlur);
                });
                System.out.println("Blur filter applied to image on file blur.jpg");
                break;
            case "6":
                measureExecutionTime(() -> {
                    filters.ConditionalBlurFilter("conditionalBlur.jpg", matrixSizeForBlur);
                });
                System.out.println("Conditional blur filter applied to image on file conditionalBlur.jpg");
                break;
            case "8":
                measureExecutionTime(() -> {
                    try {
                        filters.GrayFilterMultiThread("grayscaleMultiThread.jpg", numThreads);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("\nGrayscale filter applied to image on file grayscaleMultiThread.jpg");
                break;
            case "10":
                measureExecutionTime(() -> {
                    try {
                        filters.GlassFilterMultiThread("glassMultiThread.jpg", numThreads);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("Glass filter applied to image on file glassMultiThread.jpg");
                break;
            case "11":
                measureExecutionTime(() -> {
                    try {
                        filters.BlurFilterMultiThread("blurMultithread.jpg",matrixSizeForBlur, numThreads);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("Blur filter applied to image on file blurMultithread.jpg");
                break;
            case "12":
                measureExecutionTime(() -> {
                    try {
                        filters.ConditionalBlurFilterMultiThread("conditionalBlurMultiThread.jpg", numThreads, matrixSizeForBlur);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("Conditional blur filter applied to image on file conditionalBlurMultiThread.jpg");
                break;
            case "14":
                measureExecutionTime(() -> {
                    try {
                        filters.GrayFilterThreadPool("grayscaleThreadPool.jpg", numThreads);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("\nGrayscale filter applied to image on file grayscaleThreadPool.jpg");
                break;
            case "16":
                measureExecutionTime(() -> {
                    try {
                        filters.GlassFilterThreadPool("glassThreadPool.jpg", numThreads);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case "17":
                measureExecutionTime(() -> {
                    try {
                        filters.BlurFilterThreadPool("blurThreadPool.jpg",matrixSizeForBlur,numThreads);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                System.out.println("\nBlur filter applied to image on file blurThreadPool.jpg");
                break;
            case "18":
                measureExecutionTime(() -> {
                    try {
                        filters.ConditionalBlurFilterThreadPool("conditionalBlurThreadPool.jpg", numThreads, matrixSizeForBlur);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("\nConditional blur filter applied to image on file conditionalBlurThreadPool.jpg");
                break;
            default:
                System.out.println("\nInvalid filter");
                break;
        }
    }

    public static void measureExecutionTime(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        System.out.println("\nExecution time: " + (endTime - startTime) + " milliseconds");
    }
}