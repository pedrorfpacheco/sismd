import java.io.IOException;
import java.util.Scanner;

public class ApplyFilters {

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner input = new Scanner(System.in);
        System.out.println("Insert the name of the file path you would like to use.");
        String filePath = input.nextLine();

        System.out.println("\nInsert what filter you would like to apply to the image." +
                "\n-----Sequencial Filters-----" +
                "\n1. Brighter Filter" +
                "\n2. GrayScale Filter" +
                "\n3. Swirl Filter" +
                "\n4. Glass Filter" +
                "\n5. Blur Filter" +
                "\n6. Conditional Blur Filter" +
                "\n\n-----MultiThread Filters-----" +
                "\n7. Brighter Filter" +
                "\n8. GrayScale Filter" +
                "\n9. Swirl Filter" +
                "\n10. Glass Filter" +
                "\n11. Blur Filter" +
                "\n12. Conditional Blur Filter" +
                "\n\n-----Thread Pool Filters-----" +
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
                filters.BrighterFilter("brighter.jpg", 128);
                //multiThreadFilters.BrighterFilter("brighter.jpg", 128, 8);
                break;
            case "2":
                filters.GrayScaleFilter("grayscale.jpg");

                System.out.println("\nGrayscale filter applied to image on file grayscale.jpg");
                break;
            case "4":
                long startTime = System.currentTimeMillis(); // Início do timer
                //filters.GlassFilter("glass.jpg");
                filters.GlassFilterMultiThread("glassMultiThread.jpg", 8);
                long endTime = System.currentTimeMillis(); // Fim do timer
                System.out.println("\nTempo de execução do filtro: " + (endTime - startTime) + " milissegundos");

                System.out.println("Glass filter applied to image on file glass.jpg");
                break;
            case "10":
                measureExecutionTime(() -> {
                    try {
                        filters.GlassFilterMultiThread("glassMultiThread.jpg", 6);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("Glass filter applied to image on file glassMultiThread.jpg");
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