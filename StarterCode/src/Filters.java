import threadpool.CompletableFutures.BlurCompletableFutureTask;
import threadpool.CompletableFutures.ConditionalBlurCompletableFutureTask;
import threadpool.CompletableFutures.GlassCompletableFuturesTask;
import threadpool.CompletableFutures.GrayCompletableFuturesTask;
import threadpool.Executor.BlurFilterTask;
import threadpool.Executor.ConditionalBlurTask;
import threadpool.Executor.GlassFilterTask;
import threadpool.Executor.GrayFilterTask;
import threadpool.ForkJoinPool.*;
import threads.*;
import utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

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

    // ##### IMPERATIVE #####
    // Brighter filter works by adding value to each of the red, green and blue of each pixel
    // up to the maximum of 255
    public void BrighterFilter(String outputFile, int value) {
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

    public void BlurFilter(String outputFile, int matrixSize) {
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

    public void SwirlFilter(String outputFile) {
        int height = image.length;
        int width = image[0].length;
        int x0 = width / 2;
        int y0 = height / 2;
        double maxAngle = Math.PI / 256;

        Color[][] filteredImage = new Color[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double d = Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
                double angle = maxAngle * d;

                int xNew = (int) ((x - x0) * Math.cos(angle) - (y - y0) * Math.sin(angle) + x0);
                int yNew = (int) ((x - x0) * Math.sin(angle) + (y - y0) * Math.cos(angle) + y0);

                // Garantir que as novas coordenadas estejam dentro dos limites da imagem
                xNew = Math.max(0, Math.min(width - 1, xNew));
                yNew = Math.max(0, Math.min(height - 1, yNew));

                filteredImage[y][x] = image[yNew][xNew];
            }
        }

        Utils.writeImage(filteredImage, outputFile);
    }

    // ##### MultiThread #####

    public void BlurFilterMultiThread(String outputFile, int matrixSize, int threadCount) throws InterruptedException {
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

    public void SwirlFilterMultiThread(String outputFile, int numThreads) throws InterruptedException {
        int height = image.length;
        int width = image[0].length;
        int x0 = width / 2;
        int y0 = height / 2;
        double maxAngle = Math.PI / 256;

        Color[][] filteredImage = new Color[height][width];

        Thread[] threads = new Thread[numThreads];

        int chunkHeight = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                int startY = threadIndex * chunkHeight;
                int endY = (threadIndex == numThreads - 1) ? height : startY + chunkHeight;

                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        double d = Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
                        double angle = maxAngle * d;

                        int xNew = (int) ((x - x0) * Math.cos(angle) - (y - y0) * Math.sin(angle) + x0);
                        int yNew = (int) ((x - x0) * Math.sin(angle) + (y - y0) * Math.cos(angle) + y0);

                        xNew = Math.max(0, Math.min(width - 1, xNew));
                        yNew = Math.max(0, Math.min(height - 1, yNew));

                        filteredImage[y][x] = image[yNew][xNew];
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Utils.writeImage(filteredImage, outputFile);
    }

    public void BrighterFilterMultiThread(String outputFile, int value, int numThreads) throws IOException, InterruptedException {
        Thread[] threads = new Thread[numThreads];
        int rowsPerThread = image.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numThreads - 1) ? image.length : (startRow + rowsPerThread);
            threads[i] = new Thread(new BrighterFilterThread(image, startRow, endRow, value));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();  // Wait for all threads to finish
        }
        Utils.writeImage(image, outputFile);
    }


    // ##### Thread pool #####

    public void BrighterFilterThreadPool(String outputFile, int brightnessValue, int numThreads) throws InterruptedException {
        int height = image.length;
        int width = image[0].length;

        Color[][] filteredImage = new Color[height][width];

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int y = 0; y < height; y++) {
            final int currentY = y;
            executor.submit(() -> {
                for (int x = 0; x < width; x++) {
                    Color pixel = image[currentY][x];
                    int r = pixel.getRed();
                    int g = pixel.getGreen();
                    int b = pixel.getBlue();

                    // Adiciona o valor de brilho a cada canal de cor
                    r = Math.min(255, Math.max(0, r + brightnessValue));
                    g = Math.min(255, Math.max(0, g + brightnessValue));
                    b = Math.min(255, Math.max(0, b + brightnessValue));

                    filteredImage[currentY][x] = new Color(r, g, b);
                }
            });
        }

        executor.shutdown();

        // Aguarda até que todas as tarefas tenham sido concluídas ou que o tempo limite seja atingido
        try {
            executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("Erro ao aguardar a conclusão das tarefas: " + e.getMessage());
        }

        Utils.writeImage(filteredImage, outputFile);
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

        Utils.writeImage(glassImage, outputFile);
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
            executor.submit(new ConditionalBlurTask(image, tmp, width, startRow, endRow, matrixSize));
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Utils.writeImage(tmp, outputFile);
    }

    public void BlurFilterThreadPool(String outputFile, int matrixSize, int numThreads) throws InterruptedException {
        Color[][] blurredImage = new Color[image.length][image[0].length];
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int height = image.length;
        int width = image[0].length;
        int numTasks = numThreads;//((height * width) / numThreads) < 10000 ? numThreads : numThreads * 2;
        int chunkHeight = (height + numTasks - 1) / numTasks;

        for (int i = 0; i < numTasks; i++) {
            int startRow = i * chunkHeight;
            int endRow = Math.min(startRow + chunkHeight, height);

            executor.submit(new BlurFilterTask(image, blurredImage, startRow, endRow, matrixSize));
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Utils.writeImage(blurredImage, outputFile);
    }

    public void SwirlFilterThreadPool(String outputFile, int numThreads) {
        int height = image.length;
        int width = image[0].length;
        int x0 = width / 2;
        int y0 = height / 2;
        double maxAngle = Math.PI / 256;

        Color[][] filteredImage = new Color[height][width];

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int chunkHeight = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            executor.submit(() -> {
                int startY = threadIndex * chunkHeight;
                int endY = (threadIndex == numThreads - 1) ? height : startY + chunkHeight;

                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        double d = Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
                        double angle = maxAngle * d;

                        int xNew = (int) ((x - x0) * Math.cos(angle) - (y - y0) * Math.sin(angle) + x0);
                        int yNew = (int) ((x - x0) * Math.sin(angle) + (y - y0) * Math.cos(angle) + y0);

                        xNew = Math.max(0, Math.min(width - 1, xNew));
                        yNew = Math.max(0, Math.min(height - 1, yNew));

                        filteredImage[y][x] = image[yNew][xNew];
                    }
                }
            });
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("Erro ao aguardar a conclusão das threads: " + e.getMessage());
        }

        Utils.writeImage(filteredImage, outputFile);
    }

    // ##### Fork join #####

    public void BlurFilterForkJoinPool(String outputFile, int numThreads, int matrixSize) throws InterruptedException {
        int width = image.length;
        int height = image[0].length;
        Color[][] blurredImage = new Color[width][height];
        ForkJoinPool pool = new ForkJoinPool(numThreads);

        BlurFilterForkJoinPoolTask task = new BlurFilterForkJoinPoolTask(image, blurredImage, 0, 0, width, height, matrixSize);

        pool.invoke(task);

        Utils.writeImage(blurredImage, outputFile);
    }

    public void ConditionalBlurFilterForkJoinPool(String outputFile, int numThreads, int matrixSize) {
        int width = image.length;
        int height = image[0].length;
        Color[][] tmp = new Color[width][height];
        ForkJoinPool pool = new ForkJoinPool(numThreads);

        ConditionalBlurForkJoinPoolTask task = new ConditionalBlurForkJoinPoolTask(image, tmp, 0, 0, width, height, matrixSize);

        pool.invoke(task);

        Utils.writeImage(tmp, outputFile);
    }

    public void GrayFilterForkJoinPool(String outputFile, int numThreads) throws InterruptedException {
        Color[][] grayImage = new Color[image.length][image[0].length];

        ForkJoinPool pool = new ForkJoinPool(numThreads);

        GrayFilterForkJoinPoolTask task = new GrayFilterForkJoinPoolTask(image, grayImage, 0, 0, image.length, image[0].length);

        pool.invoke(task);

        Utils.writeImage(grayImage, outputFile);
    }

    public void GlassFilterForkJoinPool(String outputFile, int numThreads) throws InterruptedException {
        Color[][] glassImage = new Color[image.length][image[0].length];
        ForkJoinPool pool = new ForkJoinPool(numThreads);
        GlassFilterForkJoinPoolTask task = new GlassFilterForkJoinPoolTask(image, glassImage, 0, 0, image.length, image[0].length);

        pool.invoke(task);

        Utils.writeImage(glassImage, outputFile);
    }

    public void SwirlFilterForkJoinPool(String outputFile, int numThreads) {
        int height = image.length;
        int width = image[0].length;
        int x0 = width / 2;
        int y0 = height / 2;
        double maxAngle = Math.PI / 256;

        Color[][] filteredImage = new Color[height][width];

        ForkJoinPool forkJoinPool = new ForkJoinPool(numThreads);

        forkJoinPool.invoke(new SwirlFilterForkJoinPoolTask(image, filteredImage, 0, height, x0, y0, maxAngle));

        Utils.writeImage(filteredImage, outputFile);
    }

    public void BrighterFilterForkJoinPool(String outputFile, int numThreads, int brightnessValue) {
        int height = image.length;
        int width = image[0].length;

        Color[][] filteredImage = new Color[height][width];

        ForkJoinPool forkJoinPool = new ForkJoinPool(numThreads);

        forkJoinPool.invoke(new BrightnessFilterForkJoinPoolTask(image, filteredImage, 0, height, brightnessValue));

        forkJoinPool.shutdown();

        Utils.writeImage(filteredImage, outputFile);
    }

    // ##### Completable Future #####
    public void BlurFilterCompletableFuture(String outputFile, int matrixSize, int numThreads) throws InterruptedException, ExecutionException {
        int width = image.length;
        int height = image[0].length;
        int chunkWidth = (width + numThreads - 1) / numThreads;
        //int chunkWidth = width +numThreads/ numThreads;
        Color[][] tmp = new Color[width][height];
        CompletableFuture[] futures = new CompletableFuture[numThreads];

        for (int i = 0; i < numThreads; i++) {
            int startColumn = i * chunkWidth;
            int endColumn = Math.min(startColumn + chunkWidth, width);
            futures[i] = CompletableFuture.runAsync(new BlurCompletableFutureTask(image, tmp, startColumn, endColumn, height, matrixSize));
        }

        CompletableFuture.allOf(futures).join();

        Utils.writeImage(tmp, outputFile);
    }

    public void GrayFilterCompletableFuture(String outputFile, int numThreads) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int height = image.length;
        int chunkHeight = (height + numThreads - 1) / numThreads;
        List<Future<CompletableFuture<Color[][]>>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * chunkHeight;
            int endRow = Math.min(startRow + chunkHeight, height);
            futures.add(executor.submit(new GrayCompletableFuturesTask(image, startRow, endRow)));
        }

        Color[][] grayImage = new Color[image.length][image[0].length];
        for (Future<CompletableFuture<Color[][]>> future : futures) {
            CompletableFuture<Color[][]> completableFuture = future.get();
            Color[][] partialGrayImage = completableFuture.get();
            System.arraycopy(partialGrayImage, 0, grayImage, partialGrayImage.length * futures.indexOf(future), partialGrayImage.length);
        }

        executor.shutdown();
        Utils.writeImage(grayImage, outputFile);
    }

    public void GlassFilterCompletableFuture(String outputFile, int numThreads) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int height = image.length;
        int chunkHeight = (height + numThreads - 1) / numThreads;
        List<Future<CompletableFuture<Color[][]>>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * chunkHeight;
            int endRow = Math.min(startRow + chunkHeight, height);
            futures.add(executor.submit(new GlassCompletableFuturesTask(image, startRow, endRow)));
        }

        Color[][] glassImage = new Color[image.length][image[0].length];
        for (int i = 0; i < futures.size(); i++) {
            Future<CompletableFuture<Color[][]>> future = futures.get(i);
            CompletableFuture<Color[][]> completableFuture = future.get();
            Color[][] partialGlassImage = completableFuture.get();
            System.arraycopy(partialGlassImage, 0, glassImage, partialGlassImage.length * i, partialGlassImage.length);
        }

        executor.shutdown();
        Utils.writeImage(glassImage, outputFile);
    }

    public void ConditionalBlurFilterCompletableFuture(String outputFile, int matrixSize, int numThreads) {
        int width = image.length;
        int height = image[0].length;
        int chunkWidth = width / numThreads;
        Color[][] tmp = new Color[width][height];
        CompletableFuture[] futures = new CompletableFuture[numThreads];

        for (int i = 0; i < numThreads; i++) {
            int startColumn = i * chunkWidth;
            int endColumn = (i < numThreads - 1) ? startColumn + chunkWidth : width;
            futures[i] = CompletableFuture.runAsync(new ConditionalBlurCompletableFutureTask(image, tmp, startColumn, endColumn, height, matrixSize));
        }

        CompletableFuture.allOf(futures).join();

        Utils.writeImage(tmp, outputFile);

    }

    public void SwirlFilterCompletableFuture(String outputFile, int numThreads) {
        int height = image.length;
        int width = image[0].length;
        int x0 = width / 2;
        int y0 = height / 2;
        double maxAngle = Math.PI / 256;

        Color[][] filteredImage = new Color[height][width];

        CompletableFuture<Void>[] futures = new CompletableFuture[numThreads];

        int chunkHeight = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            futures[i] = CompletableFuture.runAsync(() -> {
                int startY = threadIndex * chunkHeight;
                int endY = (threadIndex == numThreads - 1) ? height : startY + chunkHeight;

                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        double d = Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
                        double angle = maxAngle * d;

                        int xNew = (int) ((x - x0) * Math.cos(angle) - (y - y0) * Math.sin(angle) + x0);
                        int yNew = (int) ((x - x0) * Math.sin(angle) + (y - y0) * Math.cos(angle) + y0);

                        xNew = Math.max(0, Math.min(width - 1, xNew));
                        yNew = Math.max(0, Math.min(height - 1, yNew));

                        filteredImage[y][x] = image[yNew][xNew];
                    }
                }
            });
        }

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futures);

        try {
            allOfFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Erro ao aguardar a conclusão das tarefas: " + e.getMessage());
        }

        Utils.writeImage(filteredImage, outputFile);
    }

    public void BrighterFilterCompletableFuture(String outputFile, int numThreads, int brightnessValue) {
        int height = image.length;
        int width = image[0].length;
        Color[][] filteredImage = new Color[height][width];

        CompletableFuture<Void>[] futures = new CompletableFuture[numThreads];

        int chunkHeight = height / numThreads;

        for (int t = 0; t < numThreads; t++) {
            final int startRow = t * chunkHeight;
            final int endRow = (t == numThreads - 1) ? height : (t + 1) * chunkHeight;

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int y = startRow; y < endRow; y++) {
                    for (int x = 0; x < width; x++) {
                        Color pixel = image[y][x];
                        int r = pixel.getRed();
                        int g = pixel.getGreen();
                        int b = pixel.getBlue();

                        // Adiciona o valor de brilho a cada canal de cor
                        r = Math.min(255, Math.max(0, r + brightnessValue));
                        g = Math.min(255, Math.max(0, g + brightnessValue));
                        b = Math.min(255, Math.max(0, b + brightnessValue));

                        filteredImage[y][x] = new Color(r, g, b);
                    }
                }
            });

            futures[t] = future;
        }

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futures);

        try {
            allOfFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Erro ao aguardar a conclusão das tarefas: " + e.getMessage());
        }

        Utils.writeImage(filteredImage, outputFile);
    }


}
