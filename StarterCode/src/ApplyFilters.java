import java.io.IOException;
import java.util.Scanner;

public class ApplyFilters {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        System.out.println("Insert the name of the file path you would like to use.");
        String filePath = input.nextLine();

        System.out.println("\nInsert what filter you would like to apply to the image." +
                "\n1. Brighter Filter" +
                "\n2. GrayScale Filter" +
                "\n3. Swirl Filter" +
                "\n4. Glass Filter" +
                "\n5. Blur Filter" +
                "\n6. Conditional Blur Filter");

        String filter = input.nextLine();
        input.close();

        applyFilter(filter, filePath);
    }

    public static void applyFilter(String filter, String filePath) throws IOException {
        Filters filters = new Filters(filePath);
        switch (filter) {
            case "1":
                filters.BrighterFilter("brighter.jpg", 128);
                System.out.println("\nBrighter filter applied to image on file brighter.jpg");
                break;
            case "2":
                filters.GrayScaleFilter("grayscale.jpg");
                System.out.println("\nGrayscale filter applied to image on file grayscale.jpg");
                break;
            case "4":
                filters.GlassFilter("glass.jpg");
                System.out.println("\nGlass filter applied to image on file glass.jpg");
                break;
            default:
                System.out.println("\nInvalid filter");
                break;
        }

    }
}