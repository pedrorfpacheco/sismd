package threads;

import java.awt.*;

public class BlurFilterThread extends Thread{

    private final Color[][] source;
    private final Color[][] destination;
    private final int startRow, endRow;
    private final int matrixSize;

    public BlurFilterThread(Color[][] source, Color[][] destination, int startRow, int endRow, int matrixSize) {
        this.source = source;
        this.destination = destination;
        this.startRow = startRow;
        this.endRow = endRow;
        this.matrixSize = matrixSize;
    }

    @Override
    public void run() {
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
