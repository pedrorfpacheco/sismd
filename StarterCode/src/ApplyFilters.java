import java.io.IOException;
import java.util.Scanner;

public class ApplyFilters {

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner input = new Scanner(System.in);
        System.out.println("Insert the name of the file path you would like to use.");
        String filePath = input.nextLine();

        System.out.println("\nInsert what filter you would like to apply to the image." +
                "\n-----Filters with Sequential Implementation-----" +
                "\n1. Brighter Filter" +
                "\n2. GrayScale Filter" +
                "\n3. Swirl Filter" +
                "\n4. Glass Filter" +
                "\n5. Blur Filter" +
                "\n6. Conditional Blur Filter" +
                "\n\n-----Filters with MultiThread Implementation-----" +
                "\n7. Brighter Filter" +
                "\n8. GrayScale Filter" +
                "\n9. Swirl Filter" +
                "\n10. Glass Filter" +
                "\n11. Blur Filter" +
                "\n12. Conditional Blur Filter" +
                "\n\n-----Filter with ThreadPool Implementation-----" +
                "\n13. Brighter Filter" +
                "\n14. GrayScale Filter" +
                "\n15. Swirl Filter" +
                "\n16. Glass Filter" +
                "\n17. Blur Filter" +
                "\n18. Conditional Blur Filter");

        String filter = input.nextLine();
        input.close();

        applyFilter(filter, filePath);
    }

    public static void applyFilter(String filter, String filePath) throws IOException, InterruptedException {
        Filters filters = new Filters(filePath);
        final int numThreads = 9;

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
            case "6":
                measureExecutionTime(() -> {
                    filters.ConditionalBlurFilter("conditionalBlur.jpg");
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
            case "12":
                measureExecutionTime(() -> {
                    try {
                        filters.ConditionalBlurFilterMultiThread("conditionalBlurMultiThread.jpg", numThreads);
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
            case "18":
                measureExecutionTime(() -> {
                    try {
                        filters.ConditionalBlurFilterThreadPool("conditionalBlurThreadPool.jpg", numThreads);
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