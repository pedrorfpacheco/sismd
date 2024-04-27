package threadpool.ForkJoinPool;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class SwirlFilterForkJoinPoolTask extends RecursiveAction {

    private final Color[][] image;
    private final Color[][] filteredImage;
    private final int startY;
    private final int endY;
    private final int x0;
    private final int y0;
    private final double maxAngle;

    public SwirlFilterForkJoinPoolTask(Color[][] image, Color[][] filteredImage, int startY, int endY, int x0, int y0, double maxAngle) {
        this.image = image;
        this.filteredImage = filteredImage;
        this.startY = startY;
        this.endY = endY;
        this.x0 = x0;
        this.y0 = y0;
        this.maxAngle = maxAngle;
    }

    @Override
    protected void compute() {
        if (endY - startY <= 1) {
            for (int y = startY; y < endY; y++) {
                for (int x = 0; x < image[0].length; x++) {
                    double d = Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
                    double angle = maxAngle * d;

                    int xNew = (int) ((x - x0) * Math.cos(angle) - (y - y0) * Math.sin(angle) + x0);
                    int yNew = (int) ((x - x0) * Math.sin(angle) + (y - y0) * Math.cos(angle) + y0);

                    xNew = Math.max(0, Math.min(image[0].length - 1, xNew));
                    yNew = Math.max(0, Math.min(image.length - 1, yNew));

                    filteredImage[y][x] = image[yNew][xNew];
                }
            }
        } else {
            int midY = (startY + endY) / 2;
            SwirlFilterForkJoinPoolTask leftTask = new SwirlFilterForkJoinPoolTask(image, filteredImage, startY, midY, x0, y0, maxAngle);
            SwirlFilterForkJoinPoolTask rightTask = new SwirlFilterForkJoinPoolTask(image, filteredImage, midY, endY, x0, y0, maxAngle);
            invokeAll(leftTask, rightTask);
        }
    }
}
