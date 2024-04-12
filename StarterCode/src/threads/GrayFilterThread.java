package threads;

import java.awt.*;

public class GrayFilterThread extends Thread {
    private Color[][] image;
    private int width;
    private int startRow;
    private int endRow;

    public GrayFilterThread(Color[][] image, int width, int startRow, int endRow) {
        this.image = image;
        this.width = width;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = 0; i < width; i++) {
            for (int j = startRow; j < endRow; j++) {
                Color pixel = image[i][j];

                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                int a = (r + g + b) / 3;

                image[i][j] = new Color(a, a, a);
            }
        }
    }
}