package threadpool.Executor;

import java.awt.*;

import static utils.Utils.BlurCondition;
import static utils.Utils.BlurPixel;

public class ConditionalBlurTask implements Runnable {
    private final Color[][] image;
    private final Color[][] tmp;
    private final int width;
    private final int startRow;
    private final int endRow;
    private final int matrixSize;

    public ConditionalBlurTask(Color[][] image, Color[][] tmp, int width, int startRow, int endRow, int matrixSize) {
        this.image = image;
        this.tmp = tmp;
        this.width = width;
        this.startRow = startRow;
        this.endRow = endRow;
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
    }
}
