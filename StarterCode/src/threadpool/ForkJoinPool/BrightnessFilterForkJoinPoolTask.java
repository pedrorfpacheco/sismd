package threadpool.ForkJoinPool;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class BrightnessFilterForkJoinPoolTask extends RecursiveAction {

    private final Color[][] image;
    private final Color[][] filteredImage;
    private final int startY;
    private final int endY;
    private final int brightnessValue;

    public BrightnessFilterForkJoinPoolTask(Color[][] image, Color[][] filteredImage, int startY, int endY, int brightnessValue) {
        this.image = image;
        this.filteredImage = filteredImage;
        this.startY = startY;
        this.endY = endY;
        this.brightnessValue = brightnessValue;
    }

    @Override
    protected void compute() {
        if (endY - startY <= 1) {
            // Processar uma linha da imagem
            for (int y = startY; y < endY; y++) {
                for (int x = 0; x < image[0].length; x++) {
                    Color pixel = image[y][x];
                    int r = pixel.getRed();
                    int g = pixel.getGreen();
                    int b = pixel.getBlue();

                    // Adiciona o valor de brilho a cada canal de cor
                    r = Math.min(255, Math.max(0, r + brightnessValue));
                    g = Math.min(255, Math.max(0, g + brightnessValue));
                    b = Math.min(255, Math.max(0, b + brightnessValue));

                    filteredImage[y][x] = new Color(r, g, b);
                }
            }
        } else {
            // Dividir o trabalho em duas tarefas
            int midY = (startY + endY) / 2;
            BrightnessFilterForkJoinPoolTask leftTask = new BrightnessFilterForkJoinPoolTask(image, filteredImage, startY, midY, brightnessValue);
            BrightnessFilterForkJoinPoolTask rightTask = new BrightnessFilterForkJoinPoolTask(image, filteredImage, midY, endY, brightnessValue);
            invokeAll(leftTask, rightTask);
        }
    }
}
