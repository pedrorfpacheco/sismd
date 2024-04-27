package threads;

import java.awt.*;

public class BrighterFilterThread implements Runnable {
    private final Color[][] source;
    private final Color[][] destination;
    private final int startRow;
    private final int endRow;
    private final int value;

    public BrighterFilterThread(Color[][] source, Color[][] destination, int startRow, int endRow, int value) {
        this.source = source;
        this.destination = destination;
        this.startRow = startRow;
        this.endRow = endRow;
        this.value = value;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < source[i].length; j++) {
                Color pixel = source[i][j];
                int r = Math.min(255, pixel.getRed() + value);
                int g = Math.min(255, pixel.getGreen() + value);
                int b = Math.min(255, pixel.getBlue() + value);
                destination[i][j] = new Color(r, g, b);
            }
        }
    }
}
