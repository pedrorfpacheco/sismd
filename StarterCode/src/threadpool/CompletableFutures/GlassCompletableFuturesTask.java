package threadpool.CompletableFutures;

import java.awt.Color;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.Random;

public class GlassCompletableFuturesTask implements Callable<CompletableFuture<Color[][]>> {
    private Color[][] image;
    private int startRow;
    private int endRow;
    private Random random;

    public GlassCompletableFuturesTask(Color[][] image, int startRow, int endRow) {
        this.image = image;
        this.startRow = startRow;
        this.endRow = endRow;
        this.random = new Random();
    }

    @Override
    public CompletableFuture<Color[][]> call() {
        return CompletableFuture.supplyAsync(() -> {
            Color[][] glassImage = new Color[endRow - startRow][image[0].length];
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < image[0].length; j++) {
                    int randomX = i + random.nextInt(5) - 2;
                    int randomY = j + random.nextInt(5) - 2;
                    if (randomX < 0 || randomX >= image.length || randomY < 0 || randomY >= image[0].length) {
                        glassImage[i - startRow][j] = image[i][j];
                    } else {
                        glassImage[i - startRow][j] = image[randomX][randomY];
                    }
                }
            }
            return glassImage;
        });
    }
}