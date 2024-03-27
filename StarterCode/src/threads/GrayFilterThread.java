package threads;

import java.awt.*;

public class GrayFilterThread extends Thread {
    private Color[][] image;
    private int width;
    private int height;
    private int startIndex;
    private int step;

    public GrayFilterThread(Color[][] image, int width, int height, int startIndex, int step) {
        this.image = image;
        this.width = width;
        this.height = height;
        this.startIndex = startIndex;
        this.step = step;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < width; i += step) {
            for (int j = 0; j < height; j++) {
                Color pixel = image[i][j];

                int a = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;

                image[i][j] = new Color(a, a, a);
            }
        }
    }
}
