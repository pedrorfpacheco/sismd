package threadpool;

import java.awt.*;

public class GlassFilterTask implements Runnable {
    private Color[][] image;
    private int width;
    private int height;
    private int radius;
    private int startRow;
    private int endRow;

    public GlassFilterTask(Color[][] image, int width, int height, int radius, int startRow, int endRow) {
        this.image = image;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = 0; i < width; i++) {
            for (int j = startRow; j < endRow; j++) {
                int dx = (int) (Math.random() * radius * 2 - radius);
                int dy = (int) (Math.random() * radius * 2 - radius);
                int newX = i + dx;
                int newY = j + dy;
                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    image[i][j] = image[newX][newY];
                }
            }
        }
    }
}
