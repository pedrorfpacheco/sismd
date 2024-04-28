package threadpool.CompletableFutures;

import java.awt.*;

public class GrayCompletableFuturesTask implements Runnable {
    private Color[][] image;
    private Color[][] destination;
    private int startRow;
    private int endRow;

    public GrayCompletableFuturesTask(Color[][] image, Color[][] destination, int startRow, int endRow) {
        this.image = image;
        this.destination = destination;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < image[0].length; j++) {
                Color color = image[i][j];

                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                destination[i][j] = new Color(gray, gray, gray);
            }
        }
    }
}
