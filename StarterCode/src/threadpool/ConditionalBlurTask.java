package threadpool;

import java.awt.*;

import static utils.Utils.BlurCondition;
import static utils.Utils.BlurPixel;

public class ConditionalBlurTask implements Runnable {
    private final Color[][] image;
    private final Color[][] tmp;
    private final int width;
    private final int startRow;
    private final int endRow;

    public ConditionalBlurTask(Color[][] image, Color[][] tmp, int width, int startRow, int endRow) {
        this.image = image;
        this.tmp = tmp;
        this.width = width;
        this.startRow = startRow;
        this.endRow = endRow;
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
    }
}
