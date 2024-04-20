package threadpool.CompletableFutures;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class GrayCompletableFuturesTask implements Callable<CompletableFuture<Color[][]>> {
    private Color[][] image;
    private int startRow;
    private int endRow;

    public GrayCompletableFuturesTask(Color[][] image, int startRow, int endRow) {
        this.image = image;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public CompletableFuture<Color[][]> call() {
        return CompletableFuture.supplyAsync(() -> {
            Color[][] grayImage = new Color[endRow - startRow][image[0].length];
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < image[0].length; j++) {
                    Color color = image[i][j];
                    int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    grayImage[i - startRow][j] = new Color(gray, gray, gray);
                }
            }
            return grayImage;
        });
    }
}
