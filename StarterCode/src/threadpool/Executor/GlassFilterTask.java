package threadpool.Executor;

import java.awt.*;
import java.util.Random;

public class GlassFilterTask implements Runnable {
    private Color[][] image;
    private Color[][] destination;
    private int[][] dx;
    private int[][] dy;
    private int startRow;
    private int endRow;
    private Random random = new Random();

    public GlassFilterTask(Color[][] image, Color[][] destination, int[][] dx, int[][] dy, int startRow, int endRow) {
        this.image = image;
        this.destination = destination;
        this.dx = dx;
        this.dy = dy;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = 0; i < image.length; i++) {
            for (int j = startRow; j < endRow; j++) {
                int newI = Math.min(Math.max(i + dx[i][j], 0), image.length - 1);
                int newJ = Math.min(Math.max(j + dy[i][j], 0), image[0].length - 1);
                destination[i][j] = image[newI][newJ];
            }
        }
    }
}
