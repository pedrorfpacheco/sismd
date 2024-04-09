package threads;

import java.awt.*;
import java.util.concurrent.CountDownLatch;

import static utils.Utils.BlurCondition;
import static utils.Utils.BlurPixel;

public class ConditionalBlurThread extends Thread {

    private final Color[][] image;

    private final Color[][] tmp;

    private final int width;

    private final int height;

    private final int startRow;

    private final int endRow;

    CountDownLatch latch;

    public ConditionalBlurThread(Color[][] image, Color[][] tmp, int width, int height, int startRow, int endRow, CountDownLatch latch) {
        this.image = image;
        this.tmp = tmp;
        this.width = width;
        this.height = height;
        this.startRow = startRow;
        this.endRow = endRow;
        this.latch = latch;
    }

    @Override
    public void run() {
        for (int c = 0; c < width; c++) {
            for (int l = startRow; l < endRow; l++) {
                Color pixel = image[c][l];
                if (BlurCondition(pixel)) {
                    tmp[c][l] = BlurPixel(image, c, l);
                }
            }
        }
        latch.countDown();
    }
}
