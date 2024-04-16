package threads;

import java.awt.*;
import java.util.concurrent.CountDownLatch;

import static utils.Utils.BlurCondition;
import static utils.Utils.BlurPixel;

public class ConditionalBlurThread extends Thread {
    private final Color[][] image;
    private final Color[][] tmp;
    private final int width;
    private final int startRow;
    private final int endRow;
    CountDownLatch latch;
    private final int matrixSize;

    public ConditionalBlurThread(Color[][] image, Color[][] tmp, int width, int startRow, int endRow, CountDownLatch latch, int matrixSize) {
        this.image = image;
        this.tmp = tmp;
        this.width = width;
        this.startRow = startRow;
        this.endRow = endRow;
        this.latch = latch;
        this.matrixSize = matrixSize;
    }

    @Override
    public void run() {
        for (int c = 0; c < width; c++) {
            for (int l = startRow; l < endRow; l++) {
                Color pixel = image[c][l];

                if (BlurCondition(pixel)) {
                    pixel = BlurPixel(image, c, l, matrixSize);
                }

                tmp[c][l] = pixel;
            }
        }
        latch.countDown();
    }
}
