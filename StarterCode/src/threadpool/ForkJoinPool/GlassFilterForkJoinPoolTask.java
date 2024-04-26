package threadpool.ForkJoinPool;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class GlassFilterForkJoinPoolTask extends RecursiveAction {
    private static final int THRESHOLD = 50000;
    private final Color[][] image;
    private final Color[][] destination;
    private final int startX, startY, endX, endY;
    private Random random = new Random();

    public GlassFilterForkJoinPoolTask(Color[][] image, Color[][] tmp, int startX, int startY, int endX, int endY) {
        this.image = image;
        this.destination = tmp;
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
                    new GlassFilterForkJoinPoolTask(image, destination, startX, startY, midX, midY), // Top left quadrant
                    new GlassFilterForkJoinPoolTask(image, destination, midX, startY, endX, midY), // Top right quadrant
                    new GlassFilterForkJoinPoolTask(image, destination, startX, midY, midX, endY), // Bottom left quadrant
                    new GlassFilterForkJoinPoolTask(image, destination, midX, midY, endX, endY) // Bottom right quadrant
            );
        }
    }

    private void applyFilter() {
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
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
