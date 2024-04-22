package threadpool.ForkJoinPool;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class BlurFilterForkJoinPoolTask extends RecursiveAction {

    private static final int THRESHOLD = 1000;
    private final Color[][] source;
    private final Color[][] destination;
    private final int startRow, endRow;
    private final int matrixSize; // Size of the blur matrix

    public BlurFilterForkJoinPoolTask(Color[][] source, Color[][] destination, int startRow, int endRow, int matrixSize) {
        this.source = source;
        this.destination = destination;
        this.startRow = startRow;
        this.endRow = endRow;
        this.matrixSize = matrixSize;
    }

    @Override
    protected void compute() {
        int length = endRow - startRow;
        if (length < THRESHOLD) {
            // Process sequentially
            applyBlur(source, destination, startRow, endRow, matrixSize);
        } else {
            // Split task and process in parallel
            int mid = startRow + length / 2;
            invokeAll(
                    new BlurFilterForkJoinPoolTask(source, destination, startRow, mid, matrixSize),
                    new BlurFilterForkJoinPoolTask(source, destination, mid, endRow, matrixSize)
            );
        }
    }

    public static void applyBlur(Color[][] source, Color[][] destination, int startRow, int endRow, int matrixSize) {
        int height = source.length;
        int width = source[0].length;
        int offset = matrixSize / 2;

        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < width; j++) {
                int redSum = 0, greenSum = 0, blueSum = 0;
                int count = 0;

                for (int ki = -offset; ki <= offset; ki++) {
                    for (int kj = -offset; kj <= offset; kj++) {
                        int ni = i + ki;
                        int nj = j + kj;

                        if (ni >= 0 && ni < height && nj >= 0 && nj < width) {
                            Color neighborColor = source[ni][nj];
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

                destination[i][j] = new Color(avgRed, avgGreen, avgBlue);
            }
        }
    }

}
