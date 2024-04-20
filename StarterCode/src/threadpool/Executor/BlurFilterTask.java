package threadpool.Executor;

import java.awt.*;

public class BlurFilterTask implements Runnable{
    private final Color[][] source;
    private final Color[][] destination;
    private final int startRow, endRow;
    private final int m;

    public BlurFilterTask(Color[][] source, Color[][] destination, int startRow, int endRow, int m) {
        this.source = source;
        this.destination = destination;
        this.startRow = startRow;
        this.endRow = endRow;
        this.m = m;
    }

    @Override
    public void run() {
        int height = source.length;
        int width = source[0].length;
        int offset = m / 2;

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
