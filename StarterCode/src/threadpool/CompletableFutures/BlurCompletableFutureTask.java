package threadpool.CompletableFutures;

import java.awt.*;
import static utils.Utils.BlurPixel;

public class BlurCompletableFutureTask implements Runnable {
    private final Color[][] image;
    private final Color[][] tmp;
    private final int startColumn;
    private final int endColumn;
    private final int height;
    private final int matrixSize;

    public BlurCompletableFutureTask(Color[][] image, Color[][] tmp, int startColumn, int endColumn, int height, int matrixSize) {
        this.image = image;
        this.tmp = tmp;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
        this.height = height;
        this.matrixSize = matrixSize;
    }

    @Override
    public void run() {
        for (int c = startColumn; c < endColumn; c++) {
            for (int l = 0; l < height; l++) {
                Color pixel = image[c][l];
                pixel = BlurPixel(image, c, l, matrixSize);
                tmp[c][l] = pixel;
            }
        }
    }
}
