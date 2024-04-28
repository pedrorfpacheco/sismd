package threadpool.CompletableFutures;

import java.awt.*;

public class GrayCompletableFuturesTask implements Runnable {
    private Color[][] image;
    private int startRow;
    private int endRow;

    public GrayCompletableFuturesTask(Color[][] image, int startRow, int endRow) {
        this.image = image;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        Color[][] grayImage = new Color[endRow - startRow][image[0].length];

        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < image[0].length; j++) {
                Color color = image[i][j];

                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                grayImage[i - startRow][j] = new Color(gray, gray, gray);
            }
        }
    }
}
