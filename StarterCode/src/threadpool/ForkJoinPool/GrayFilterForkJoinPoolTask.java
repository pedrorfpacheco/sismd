package threadpool.ForkJoinPool;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class GrayFilterForkJoinPoolTask extends RecursiveAction {
    private static final int THRESHOLD = 50000;

    private Color[][] image;
    private Color[][] destination;
    private final int startX, startY, endX, endY;

    public GrayFilterForkJoinPoolTask(Color[][] image, Color[][] destination, int startX, int startY, int endX, int endY) {
        this.image = image;
        this.destination = destination;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    protected void compute() {
        int size = (endX - startX) * (endY - startY);
        if (size < THRESHOLD) {
            applyFilter();
        } else {
            int midX = startX + (endX - startX) / 2;
            int midY = startY + (endY - startY) / 2;

            invokeAll(
                    new GrayFilterForkJoinPoolTask(image, destination, startX, startY, midX, midY), // Top left quadrant
                    new GrayFilterForkJoinPoolTask(image, destination, midX, startY, endX, midY), // Top right quadrant
                    new GrayFilterForkJoinPoolTask(image, destination, startX, midY, midX, endY), // Bottom left quadrant
                    new GrayFilterForkJoinPoolTask(image, destination, midX, midY, endX, endY) // Bottom right quadrant
            );
        }
    }

    private void applyFilter() {
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                Color color = image[i][j];

                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                destination[i][j] = new Color(gray, gray, gray);
            }
        }
    }
}
