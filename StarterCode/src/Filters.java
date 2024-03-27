import threadpool.GlassFilterTask;
import threadpool.GrayFilterTask;
import threads.GlassFilterThread;
import threads.GrayFilterThread;
import utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public void GrayFilterMultiThread(String outputfile, int numThreads) throws InterruptedException {
        Color[][] tmp = Utils.copyImage(image);

        int width = tmp.length;
        int height = tmp[0].length;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new GrayFilterThread(tmp, width, height, i, numThreads);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Utils.writeImage(tmp, outputfile);
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

    public void GrayFilterThreadPool(String outputFile, int numThreads) throws IOException, InterruptedException {
        Color[][] tmp = Utils.copyImage(image);

        int width = tmp.length;
        int height = tmp[0].length;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.execute(new GrayFilterTask(tmp, width, height, i, numThreads));
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Utils.writeImage(tmp, outputFile);
    }

    public void GlassFilterThreadPool(String outputFile, int numThreads) throws IOException, InterruptedException {
        Color[][] tmp = Utils.copyImage(image);

        int width = tmp.length;
        int height = tmp[0].length;
        int radius = 5;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int numRowsPerThread = height / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startRow = i * numRowsPerThread;
            int endRow = (i == numThreads - 1) ? height : (i + 1) * numRowsPerThread;
            executor.execute(new GlassFilterTask(tmp, width, height, radius, startRow, endRow));
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Utils.writeImage(tmp, outputFile);
    }

}
