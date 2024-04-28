import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ApplyFilters {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        System.out.println("Welcome to the Image Filter Application!");
        System.out.println("Choose your option:\n");
        System.out.println("1. Apply a filter to an image");
        System.out.println("2. Run tests for the filters");
        String option = input.nextLine();

        switch (option) {
            case "1":
                menu();
                break;
            case "2":
                runTests();
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
    }

    public static void menu() throws IOException, ExecutionException, InterruptedException {
        Scanner input = new Scanner(System.in);
        System.out.println("Insert the name of the file path you would like to use.");
        String filePath = input.nextLine();

        System.out.println("\nChoose a filter to apply to the image:");
        System.out.format("|     Sequential      |     MultiThread      |                             Threadpool                             | %n");
        System.out.format("|---------------------|----------------------|       Executor       |     ForkJoinPool     |  CompletableFutures  |%n");
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
        final int numThreads = 7;
        final int matrixSizeForBlur = 3;
        final int brighterIntensity = 128;

        switch (filter) {
            case "1":
                long startTime1 = System.currentTimeMillis();

                filters.BrighterFilter("brighter.jpg", brighterIntensity);

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
            case "3":
                long startTime3 = System.currentTimeMillis();

                filters.SwirlFilter("swirl.jpg");

                long endTime3 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime3 - startTime3) + " milliseconds");

                System.out.println("\nSwirl filter applied to image on file swirl.jpg");
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
            case "7":
                long startTime7 = System.currentTimeMillis();

                filters.BrighterFilterMultiThread("brighterMultiThread.jpg", brighterIntensity, numThreads);

                long endTime7 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime7 - startTime7) + " milliseconds");

                System.out.println("Brighter filter applied to image on file brighterMultiThread.jpg");
                break;
            case "8":
                long startTime8 = System.currentTimeMillis();

                filters.GrayFilterMultiThread("grayscaleMultiThread.jpg", numThreads);

                long endTime8 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime8 - startTime8) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscaleMultiThread.jpg");
                break;
            case "9":
                long startTime9 = System.currentTimeMillis();
                int threads = Runtime.getRuntime().availableProcessors();

                filters.SwirlFilterMultiThread("swirlMultithreading.jpg", numThreads);

                long endTime9 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime9 - startTime9) + " milliseconds");

                System.out.println("\nSwirl filter applied to image on file swirlMultithreading.jpg");
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
            case "13":
                long startTime13 = System.currentTimeMillis();

                filters.BrighterFilterThreadPool("brighterThreadPool.jpg", brighterIntensity, numThreads);

                long endTime13 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime13 - startTime13) + " milliseconds");

                System.out.println("Brighter filter applied to image on file brighterMultiThread.jpg");
                break;
            case "14":
                long startTime14 = System.currentTimeMillis();

                filters.GrayFilterThreadPool("grayscaleThreadPool.jpg", numThreads);

                long endTime14 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime14 - startTime14) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscaleThreadPool.jpg");
                break;
            case "15":
                long startTime15 = System.currentTimeMillis();

                filters.SwirlFilterThreadPool("swirlThreadPool.jpg", numThreads);

                long endTime15 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime15 - startTime15) + " milliseconds");

                System.out.println("\nSwirl filter applied to image on file swirlThreadPool.jpg");
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
            case "19":
                long startTime19 = System.currentTimeMillis();

                filters.BrighterFilterForkJoinPool("brighterForkJoinPool.jpg", numThreads, brighterIntensity);

                long endTime19 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime19 - startTime19) + " milliseconds");

                System.out.println("\nBrighter filter applied to image on file brighterForkJoinPool.jpg");
                break;
            case "20":
                long startTime20 = System.currentTimeMillis();

                filters.GrayFilterForkJoinPool("grayscaleForkJoinPool.jpg", numThreads);

                long endTime20 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime20 - startTime20) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscaleForkJoinPool.jpg");
                break;
            case "21":
                long startTime21 = System.currentTimeMillis();

                filters.SwirlFilterForkJoinPool("swirlForkJoinPool.jpg", numThreads);

                long endTime21 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime21 - startTime21) + " milliseconds");

                System.out.println("\nSwirl filter applied to image on file swirlForkJoinPool.jpg");
                break;
            case "22":
                long startTime22 = System.currentTimeMillis();

                filters.GlassFilterForkJoinPool("glassForkJoinPool.jpg", numThreads);

                long endTime22 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime22 - startTime22) + " milliseconds");

                System.out.println("\nGlass filter applied to image on file glassForkJoinPool.jpg");
                break;
            case "23":
                long startTime23 = System.currentTimeMillis();

                filters.BlurFilterForkJoinPool("blurForkJoinPool.jpg", numThreads, matrixSizeForBlur);

                long endTime23 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime23 - startTime23) + " milliseconds");

                System.out.println("\nBlur filter applied to image on file blurForkJoinPool.jpg");
                break;
            case "24":
                long startTime24 = System.currentTimeMillis();

                filters.ConditionalBlurFilterForkJoinPool("conditionalBlurForkJoinPool.jpg", numThreads, matrixSizeForBlur);

                long endTime24 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime24 - startTime24) + " milliseconds");

                System.out.println("\nConditional Blur filter applied to image on file conditionalBlurForkJoinPool.jpg");
                break;
            case "25":
                long startTime25 = System.currentTimeMillis();

                filters.BrighterFilterCompletableFuture("brighterCompletableFutures.jpg", numThreads, brighterIntensity);

                long endTime25 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime25 - startTime25) + " milliseconds");

                System.out.println("\nBrighter filter applied to image on file brighterCompletableFutures.jpg");
                break;
            case "26":
                long startTime26 = System.currentTimeMillis();

                filters.GrayFilterCompletableFuture("grayscaleCompletableFutures.jpg", numThreads);

                long endTime26 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime26 - startTime26) + " milliseconds");

                System.out.println("\nGrayscale filter applied to image on file grayscaleCompletableFutures.jpg");
                break;
            case "27":
                long startTime27 = System.currentTimeMillis();

                filters.SwirlFilterCompletableFuture("swirlCompletableFutures.jpg", numThreads);

                long endTime27 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime27 - startTime27) + " milliseconds");

                System.out.println("\nSwirl filter applied to image on file swirlCompletableFutures.jpg");
                break;
            case "28":
                long startTime28 = System.currentTimeMillis();

                filters.GlassFilterCompletableFuture("glassCompletableFutures.jpg", numThreads);

                long endTime28 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime28 - startTime28) + " milliseconds");

                System.out.println("\nGlass filter applied to image on file glassCompletableFutures.jpg");
                break;
            case "29":
                long startTime29 = System.currentTimeMillis();

                filters.BlurFilterCompletableFuture("blurCompletableFutures.jpg", matrixSizeForBlur, numThreads);

                long endTime29 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime29 - startTime29) + " milliseconds");

                System.out.println("\nBlur filter applied to image on file blurCompletableFutures.jpg");
                break;
            case "30":
                long startTime30 = System.currentTimeMillis();

                filters.ConditionalBlurFilterCompletableFuture("conditionalBlurCompletableFutures.jpg", matrixSizeForBlur, numThreads);

                long endTime30 = System.currentTimeMillis();
                System.out.println("\nExecution time: " + (endTime30 - startTime30) + " milliseconds");

                System.out.println("\nConditional Blur filter applied to image on file conditionalBlurCompletableFutures.jpg");
                break;
            default:
                System.out.println("\nInvalid filter");
                break;
        }
    }

    private static void runTests() throws InterruptedException {
        final String filePath = chooseImage();
        final String filter = chooseFilter();

        switch (filter) {
            case "1":
                BrightTestsReport brighterTestsReport = new BrightTestsReport();
                brighterTestsReport.runTests(filePath);
            case "2":
                GrayTestsReport grayTestsReport = new GrayTestsReport();
                grayTestsReport.runTests(filePath);
            case "3":
                SwirlTestsReport swirlTestsReport = new SwirlTestsReport();
                swirlTestsReport.runTests(filePath);
                break;
            case "4":
                break;
            case "5":
                BlurTestsReport blurTestsReport = new BlurTestsReport();
                blurTestsReport.runTests(filePath);
                break;

            default:
                System.out.println("Invalid filter");
        }
    }

    private static String chooseImage() {
        Scanner input = new Scanner(System.in);

        System.out.println("\nChoose an image to apply the filter to:");
        System.out.println("1. city.jpg");
        System.out.println("2. tree.jpg");
        System.out.println("3. turtle.jpg");
        System.out.println("4. monkey.jpg");
        System.out.println("5. eye.jpg");

        String filePath = input.nextLine();

        switch (filePath) {
            case "1":
                filePath = "./StarterCode/city.jpg";
                break;
            case "2":
                filePath = "./StarterCode/tree.jpg";
                break;
            case "3":
                filePath = "./StarterCode/turtle.jpg";
                break;
            case "4":
                filePath = "./StarterCode/monkey.jpg";
                break;
            case "5":
                filePath = "./StarterCode/eye.jpg";
                break;
            default:
                System.out.println("Invalid image");
                return filePath;
        }

        return filePath;
    }

    private static String chooseFilter() {

        System.out.println("\nChoose a filter to apply to the image:");
        System.out.println("1. Brighter");
        System.out.println("2. GrayScale");
        System.out.println("3. Swirl");
        System.out.println("4. Glass");
        System.out.println("5. Blur");
        System.out.println("6. Conditional Blur");

        String filter = input.nextLine();
        input.close();

        return filter;
    }
}
