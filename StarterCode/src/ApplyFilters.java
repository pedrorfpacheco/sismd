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

                System.out.println("Glass filter applied to image on file glass.jpg");
                break;
            case "8":
                measureExecutionTime(() -> {
                    try {
                        filters.GrayFilterMultiThread("grayscaleMultiThread.jpg", 4);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("\nGrayscale filter applied to image on file grayscaleMultiThread.jpg");
                break;
            case "10":
                measureExecutionTime(() -> {
                    try {
                        filters.GlassFilterMultiThread("glassMultiThread.jpg", 4);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("Glass filter applied to image on file glassMultiThread.jpg");
                break;
            case "16":
                    measureExecutionTime(() -> {
                        try {
                            filters.GlassFilterThreadPool("glassThreadPool.jpg", 4);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
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