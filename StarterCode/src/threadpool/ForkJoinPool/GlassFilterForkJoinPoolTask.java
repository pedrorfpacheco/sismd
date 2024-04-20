package threadpool.ForkJoinPool;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class GlassFilterForkJoinPoolTask extends RecursiveAction {
    private static final int THRESHOLD = 1000;

    private Color[][] image;
    private Color[][] destination;
    private int startRow;
    private int endRow;
    private Random random;

    public GlassFilterForkJoinPoolTask(Color[][] image, Color[][] destination, int startRow, int endRow) {
        this.image = image;
        this.destination = destination;
        this.startRow = startRow;
        this.endRow = endRow;
        this.random = new Random();
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= THRESHOLD) {
            applyFilter();
        } else {
            int midRow = (startRow + endRow) / 2;

            GlassFilterForkJoinPoolTask task1 = new GlassFilterForkJoinPoolTask(image, destination, startRow, midRow);
            GlassFilterForkJoinPoolTask task2 = new GlassFilterForkJoinPoolTask(image, destination, midRow, endRow);

            invokeAll(task1, task2);
        }
    }

    private void applyFilter() {
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
