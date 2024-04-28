package threadpool.CompletableFutures;

import java.awt.*;
import java.util.Random;

public class GlassCompletableFuturesTask implements Runnable {
    private Color[][] image;
    private Color[][] destination;
    private int startRow;
    private int endRow;
    private Random random;

    public GlassCompletableFuturesTask(Color[][] image, Color[][] destination, int startRow, int endRow) {
        this.image = image;
        this.destination = destination;
        this.startRow = startRow;
        this.endRow = endRow;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < image[0].length; j++) {
                int randomX = i + random.nextInt(5) - 2;
                int randomY = j + random.nextInt(5) - 2;
                if (randomX < 0 || randomX >= image.length || randomY < 0 || randomY >= image[0].length) {
                    destination[i][j] = image[i][j];
                } else {
                    destination[i][j] = image[randomX][randomY];
                }
            }
        }
    }
}