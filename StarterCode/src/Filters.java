import threadpool.BlurFilterTask;
import threadpool.ConditionalBlurTask;
import threadpool.GlassFilterTask;
import threadpool.GrayFilterTask;
import threads.BlurFilterThread;
import threads.ConditionalBlurThread;
import threads.GlassFilterThread;
import threads.GrayFilterThread;
import utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static utils.Utils.*;

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
        Color[][] tmp = copyImage(image);

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
        Color[][] tmp = copyImage(image);

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
        Color[][] tmp = copyImage(image);

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

    // Conditional blur consists in applying Blur only when some
    // condition is satisfied.
    public void ConditionalBlurFilter(String outputFile, int matrixSize) {
        int width = image.length;
        int height = image[0].length;
        Color[][] tmp = new Color[width][height];

        // Runs through entire matrix
        for (int c = 0; c < width; c++) {
            for (int l = 0; l < height; l++) {

                // get current pixel
                Color pixel = image[c][l];

                // Apply blur only when condition is satisfied
                if (BlurCondition(pixel)) {
                    pixel = BlurPixel(image, c, l, matrixSize);
                }

                // Save pixel to new image
                tmp[c][l] = pixel;
            }
        }
        // Save new image to file
        Utils.writeImage(tmp, outputFile);
    }

    public void GrayFilterMultiThread(String outputfile, int numThreads) throws InterruptedException {
        int width = image.length;
        int height = image[0].length;

        Thread[] threads = new Thread[numThreads];
        int rowsPerThread = height / numThreads;
        int remainingRows = height % numThreads;

        int startRow = 0;
        for (int i = 0; i < numThreads; i++) {
            int rowsForThisThread = rowsPerThread + (i < remainingRows ? 1 : 0);
            int endRow = startRow + rowsForThisThread;
            threads[i] = new GrayFilterThread(image, width, startRow, endRow);
            threads[i].start();
            startRow = endRow;
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Utils.writeImage(image, outputfile);
    }

    public void GlassFilterMultiThread(String outputfile, int numThreads) throws InterruptedException {
        int width = image.length;
        int height = image[0].length;
        int radius = 5;

        CountDownLatch latch = new CountDownLatch(numThreads);

        int numRowsPerThread = height / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startRow = i * numRowsPerThread;
            int endRow = (i == numThreads - 1) ? height : (i + 1) * numRowsPerThread;
            Thread thread = new GlassFilterThread(image, width, height, radius, outputfile, latch, startRow, endRow);
            thread.start();
        }

        latch.await();
        Utils.writeImage(image, outputfile);
    }

    public void ConditionalBlurFilterMultiThread(String outputFile, int numThreads, int matrixSize) throws InterruptedException {
        int width = image.length;
        int height = image[0].length;
        Color[][] tmp = new Color[width][height];
        CountDownLatch latch = new CountDownLatch(numThreads);

        int numRowsPerThread = height / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startRow = i * numRowsPerThread;
            int endRow = (i == numThreads - 1) ? height : (i + 1) * numRowsPerThread;
            Thread thread = new ConditionalBlurThread(image, tmp, width, startRow, endRow, latch, matrixSize);
            thread.start();
        }

        latch.await();
        Utils.writeImage(tmp, outputFile);
    }

    public void GrayFilterThreadPool(String outputFile, int numThreads) throws IOException, InterruptedException {
        int width = image.length;
        int height = image[0].length;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int rowsPerTask = height / numThreads;
        int remainingRows = height % numThreads;

        int startRow = 0;
        for (int i = 0; i < numThreads; i++) {
            int rowsForThisTask = rowsPerTask + (i < remainingRows ? 1 : 0);
            int endRow = startRow + rowsForThisTask;
            executor.submit(new GrayFilterTask(image, width, startRow, endRow));
            startRow = endRow;
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Utils.writeImage(image, outputFile);
    }

    public void GlassFilterThreadPool(String outputFile, int numThreads) throws IOException, InterruptedException {
        int width = image.length;
        int height = image[0].length;

        Color[][] glassImage = new Color[width][height];

        int radius = 5;
        int startRow = 0;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int rowsPerTask = height / numThreads;
        int remainingRows = height % numThreads;

        int[][] dx = new int[width][height];
        int[][] dy = new int[width][height];
        Random rand = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                dx[i][j] = (rand.nextInt(2 * radius + 1) - radius);
                dy[i][j] = (rand.nextInt(2 * radius + 1) - radius);
            }
        }

        for (int i = 0; i < numThreads; i++) {
            int rowsForThisTask = rowsPerTask + (i < remainingRows ? 1 : 0);
            int endRow = startRow + rowsForThisTask;
            executor.submit(new GlassFilterTask(image, glassImage, dx, dy, startRow, endRow));
            startRow = endRow;
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Utils.writeImage(image, outputFile);
    }

    public void ConditionalBlurFilterThreadPool(String outputFile, int numThreads, int matrixSize) throws InterruptedException {
        int width = image.length;
        int height = image[0].length;
        Color[][] tmp = new Color[width][height];

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int numRowsPerTask = height / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startRow = i * numRowsPerTask;
            int endRow = (i == numThreads - 1) ? height : (i + 1) * numRowsPerTask;
            executor.execute(new ConditionalBlurTask(image, tmp, width, startRow, endRow, matrixSize));
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Utils.writeImage(tmp, outputFile);
    }


    public void BlurFilterMultiThread(String outputFile,int matrixSize, int threadCount) throws InterruptedException{
        Color[][] blurredImage = new Color[image.length][image[0].length];
        Thread[] threads = new Thread[threadCount];
        int height = image.length;
        int chunkHeight = (height + threadCount - 1) / threadCount; // Divide equally among threads, rounding up

        for (int i = 0; i < threadCount; i++) {
            int startRow = i * chunkHeight;
            int endRow = Math.min(startRow + chunkHeight, height);

            threads[i] = new BlurFilterThread(image, blurredImage, startRow, endRow, matrixSize);
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join(); // Wait for all threads to finish
        }

        Utils.writeImage(blurredImage, outputFile);
    }

    public void BlurFilter(String outputFile,int matrixSize) {
        Color[][] blurredImage = new Color[image.length][image[0].length];
        int height = image.length;
        int width = image[0].length;
        int offset = matrixSize / 2;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int redSum = 0, greenSum = 0, blueSum = 0;
                int count = 0;

                // Iterate over the m x m submatrix centered at (i, j)
                for (int ki = -offset; ki <= offset; ki++) {
                    for (int kj = -offset; kj <= offset; kj++) {
                        int ni = i + ki; // New i index
                        int nj = j + kj; // New j index

                        // Check if the new indices are within the image bounds
                        if (ni >= 0 && ni < height && nj >= 0 && nj < width) {
                            Color neighborColor = image[ni][nj];
                            redSum += neighborColor.getRed();
                            greenSum += neighborColor.getGreen();
                            blueSum += neighborColor.getBlue();
                            count++;
                        }
                    }
                }

                // Calculate average values
                int avgRed = redSum / count;
                int avgGreen = greenSum / count;
                int avgBlue = blueSum / count;

                // Apply the new color to the blurred image
                blurredImage[i][j] = new Color(avgRed, avgGreen, avgBlue);
            }
        }

        Utils.writeImage(blurredImage, outputFile);

    }


    public void BlurFilterThreadPool(String outputFile, int matrixSize, int numThreads) throws InterruptedException{
        Color[][] blurredImage = new Color[image.length][image[0].length];
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int height = image.length;
        int chunkHeight = (height + numThreads - 1) / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * chunkHeight;
            int endRow = Math.min(startRow + chunkHeight, height);

            executor.execute(new BlurFilterTask(image, blurredImage, startRow, endRow, matrixSize));
        }

        executor.shutdown(); // No new tasks will be accepted
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Wait for all tasks to finish

        Utils.writeImage(blurredImage, outputFile);
    }
}

