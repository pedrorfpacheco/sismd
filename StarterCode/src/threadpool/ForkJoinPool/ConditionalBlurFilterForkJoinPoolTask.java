package threadpool.ForkJoinPool;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

import static utils.Utils.BlurCondition;
import static utils.Utils.BlurPixel;

public class ConditionalBlurFilterForkJoinPoolTask extends RecursiveAction {

    private static final int THRESHOLD = 50000;
    private final Color[][] image;
    private final Color[][] tmp;
    private final int startX, startY, endX, endY;
    private final int matrixSize;

    public ConditionalBlurFilterForkJoinPoolTask(Color[][] image, Color[][] tmp, int startX, int startY, int endX, int endY, int matrixSize) {
        this.image = image;
        this.tmp = tmp;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.matrixSize = matrixSize;
    }

    @Override
    protected void compute() {
        int size = (endX - startX) * (endY - startY);
        if (size < THRESHOLD) {
            applyFilter();
        } else {
            int midX = startX + (endX - startX) / 2;
            int midY = startY + (endY - startY) / 2;

            invokeAll(
                    new ConditionalBlurFilterForkJoinPoolTask(image, tmp, startX, startY, midX, midY, matrixSize), // Top left quadrant
                    new ConditionalBlurFilterForkJoinPoolTask(image, tmp, midX, startY, endX, midY, matrixSize), // Top right quadrant
                    new ConditionalBlurFilterForkJoinPoolTask(image, tmp, startX, midY, midX, endY, matrixSize), // Bottom left quadrant
                    new ConditionalBlurFilterForkJoinPoolTask(image, tmp, midX, midY, endX, endY, matrixSize) // Bottom right quadrant
            );
        }
    }

    public void applyFilter() {
        for (int c = startX; c < endX; c++) {
            for (int l = startY; l < endY; l++) {
                Color pixel = image[c][l];

                if (BlurCondition(pixel)) {
                    pixel = BlurPixel(image, c, l, matrixSize);
                }

                tmp[c][l] = pixel;
            }
        }
    }

}
