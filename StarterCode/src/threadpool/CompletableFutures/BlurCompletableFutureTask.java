package threadpool.CompletableFutures;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class BlurCompletableFutureTask implements Callable<CompletableFuture<Color[][]>> {
    private Color[][] image;
    private int startRow;
    private int endRow;
    private int matrixSize;

    public BlurCompletableFutureTask(Color[][] image, int matrixSize, int startRow, int endRow) {
        this.image = image;
        this.matrixSize = matrixSize;
        this.endRow = endRow;
        this.startRow = startRow;
    }

    @Override
    public CompletableFuture<Color[][]> call() {
        return CompletableFuture.supplyAsync(()->{
        Color[][] blurImage = new Color[endRow - startRow][image[0].length];
        int offset = matrixSize / 2;

        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < image[0].length; j++) {
                int redSum = 0, greenSum = 0, blueSum = 0;
                int count = 0;

                for (int ki = -offset; ki <= offset; ki++) {
                    for (int kj = -offset; kj <= offset; kj++) {
                        int ni = i + ki;
                        int nj = j + kj;

                        if (ni >= 0 && ni < image.length && nj >= 0 && nj < image[0].length) {
                                Color neighborColor = image[ni][nj];
                                redSum += neighborColor.getRed();
                                greenSum += neighborColor.getGreen();
                                blueSum += neighborColor.getBlue();
                                count++;
                        }
                    }
                }

                int avgRed = redSum / count;
                int avgGreen = greenSum / count;
                int avgBlue = blueSum / count;

                blurImage[i][j] = new Color(avgRed, avgGreen, avgBlue);
            }
        }
        return blurImage;
        });
    }
}
