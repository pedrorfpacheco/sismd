package threads;

import utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class GlassFilterThread extends Thread {
    private Color[][] image;
    private int width;
    private int height;
    private int radius;
    private String outputFile;
    private CountDownLatch latch;

    public GlassFilterThread(Color[][] image, int width, int height, int radius, String outputFile, CountDownLatch latch) {
        this.image = image;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.outputFile = outputFile;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            applyGlassFilter();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    private void applyGlassFilter() throws IOException {
        Color[][] tmp = Utils.copyImage(image);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int dx = (int) (Math.random() * radius * 2 - radius);
                int dy = (int) (Math.random() * radius * 2 - radius);

                int newX = i + dx;
                int newY = j + dy;

                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    tmp[i][j] = image[newX][newY];
                }
            }
        }

        Utils.writeImage(tmp, outputFile);
    }
}
