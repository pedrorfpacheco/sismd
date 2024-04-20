package threadpool.ForkJoinPool;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class GrayFilterForkJoinPoolTask extends RecursiveAction {
    private static final int THRESHOLD = 1000;

    private Color[][] image;
    private Color[][] destination;
    private int startRow;
    private int endRow;

    public GrayFilterForkJoinPoolTask(Color[][] image, Color[][] destination, int startRow, int endRow) {
        this.image = image;
        this.destination = destination;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= THRESHOLD) {
            applyFilter();
        } else {
            int midRow = (startRow + endRow) / 2;

            GrayFilterForkJoinPoolTask task1 = new GrayFilterForkJoinPoolTask(image, destination, startRow, midRow);
            GrayFilterForkJoinPoolTask task2 = new GrayFilterForkJoinPoolTask(image, destination, midRow, endRow);

            invokeAll(task1, task2);
        }
    }

    private void applyFilter() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < image[0].length; j++) {
                Color color = image[i][j];

                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                destination[i][j] = new Color(gray, gray, gray);
            }
        }
    }
}
