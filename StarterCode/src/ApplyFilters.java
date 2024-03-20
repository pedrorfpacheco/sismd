import java.io.IOException;
import java.util.Scanner;

public class ApplyFilters {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        System.out.println("Insert the name of the file path you would like to use.");
        String filePath = input.nextLine();

        System.out.println("Insert what filter you would like to apply to the image.");
        System.out.println("1. Brighter Filter");
        System.out.println("2. GrayScale Filter");
        System.out.println("3. Swirl Filter");
        System.out.println("4. Glass Filter");
        System.out.println("5. Blur Filter");
        System.out.println("6. Conditional Blur Filter");

        String filter = input.nextLine();
        input.close();

        applyFilter(filter, filePath);
    }

    public static void applyFilter(String filter, String filePath) throws IOException {
        Filters filters = new Filters(filePath);
        switch (filter) {
            case "1":
                filters.BrighterFilter("brighter.jpg", 128);
                System.out.println("Brighter filter applied to image on file brighter.jpg");
                break;
            case "2":
                filters.GrayScaleFilter("grayscale.jpg");
                System.out.println("Grayscale filter applied to image on file grayscale.jpg");
                break;
            case "4":
                filters.GlassFilter("glass.jpg");
                System.out.println("Glass filter applied to image on file glass.jpg");
                break;
            default:
                System.out.println("Invalid filter");
                break;
        }

    }
}