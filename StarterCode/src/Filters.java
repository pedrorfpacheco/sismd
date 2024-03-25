import threads.GlassFilterThread;
import utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Creating image filters for grayscale, brighter, swirl,
 * glass and blur effect
 *
 * @author Jorge Coelho
 * @version 1.0
 * @contact jmn@isep.ipp.pt
 */
public class Filters {

    String file;
    Color image[][];

    // Constructor with filename for source image
    Filters(String filename) {
        this.file = filename;
        image = Utils.loadImage(filename);
    }


    // Brighter filter works by adding value to each of the red, green and blue of each pixel
    // up to the maximum of 255
    public void BrighterFilter(String outputFile, int value) throws IOException {
        Color[][] tmp = Utils.copyImage(image);

        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                // fetches values of each pixel
                Color pixel = tmp[i][j];
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                // takes average of color values
                int bright = value;
                if (r + bright > 255)
                    r = 255;
                else
                    r = r + bright;
                if (g + bright > 255)
                    g = 255;
                else
                    g = g + bright;
                if (b + bright > 255)
                    b = 255;
                else
                    b = b + bright;

                // outputs average into picuture to make grayscale
                tmp[i][j] = new Color(r, g, b);

            }
        }
        Utils.writeImage(tmp, outputFile);
    }

    public void GrayScaleFilter(String outputFile) {
        Color[][] tmp = Utils.copyImage(image);

        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                // fetches values of each pixel
                Color pixel = tmp[i][j];
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                int a = (r + g + b) / 3;

                // outputs average into picture to make grayscale
                tmp[i][j] = new Color(a, a, a);

            }
        }
        Utils.writeImage(tmp, outputFile);
    }

    public void GlassFilter(String outputFile) {
        Color[][] tmp = Utils.copyImage(image);

        int width = tmp.length;
        int height = tmp[0].length;
        int radius = 5;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int dx = (int) (Math.random() * radius * 2 - radius);
                int dy = (int) (Math.random() * radius * 2 - radius);
                int newX = i + dx;
                int newY = j + dy;
                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    tmp[i][j] = image[newX][newY];
                }
            }
        }

        Utils.writeImage(tmp, outputFile);
    }

    public void GlassFilterMultiThread(String outputfile, int numThreads) throws InterruptedException {
        int width = image.length;
        int height = image[0].length;
        int radius = 5;

        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int t = 0; t < numThreads; t++) {
            Thread thread = new GlassFilterThread(image, width, height, radius, outputfile, latch);
            thread.start();
        }

        latch.await();
    }

}
