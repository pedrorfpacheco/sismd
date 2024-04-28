import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ConditionalBlurTestsReport {

    public ConditionalBlurTestsReport() {
    }

    private final int matrixSize = 3;

    public void runTests(final String filePath) throws InterruptedException {
        //runSequential(filePath);

        IntStream.rangeClosed(2, 12).forEach(numThreads -> {
            try {
                List<Long> executionTimesMultiThread = new ArrayList<>();
                List<Long> executionTimesThreadPool = new ArrayList<>();
                List<Long> executionTimesForkJoinPool = new ArrayList<>();
                List<Long> executionTimesCompletableFuture = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    Filters filters = new Filters(filePath);

                    /*long startTime = System.nanoTime();
                    filters.ConditionalBlurFilterMultiThread("outputConditionalBlurFilterMultiThread.png",matrixSize, numThreads);
                    long endTime = System.nanoTime();
                    executionTimesMultiThread.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));*/

                    /*long startTime = System.nanoTime();
                    filters.ConditionalBlurFilterThreadPool("outputConditionalBlurFilterThreadPool.png",matrixSize, numThreads);
                    long endTime = System.nanoTime();
                    executionTimesThreadPool.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));*/

                    /*long startTime = System.nanoTime();
                    filters.ConditionalBlurFilterForkJoinPool("outputConditionalBlurFilterForkJoinPool.png",numThreads, matrixSize);
                    long endTime = System.nanoTime();
                    executionTimesForkJoinPool.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));*/

                    long startTime = System.nanoTime();
                    filters.ConditionalBlurFilterCompletableFuture("outputConditionalBlurFilterCompletableFuture.png",matrixSize, numThreads);
                    long endTime = System.nanoTime();
                    executionTimesCompletableFuture.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
                }

                //double averageExecutionTimeMultiThread = executionTimesMultiThread.stream().mapToLong(val -> val).average().orElse(0.0);
                //double averageExecutionTimeThreadPool = executionTimesThreadPool.stream().mapToLong(val -> val).average().orElse(0.0);
                //double averageExecutionTimeForkJoinPool = executionTimesForkJoinPool.stream().mapToLong(val -> val).average().orElse(0.0);
                double averageExecutionTimeCompletableFuture = executionTimesCompletableFuture.stream().mapToLong(val -> val).average().orElse(0.0);

                System.out.println("\nAverage execution time for " + numThreads + " threads:");
                //System.out.println("MultiThread: " + averageExecutionTimeMultiThread + " ms");
                //System.out.println("ThreadPool: " + averageExecutionTimeThreadPool + " ms");
                //System.out.println("ForkJoinPool: " + averageExecutionTimeForkJoinPool + " ms");
                System.out.println("CompletableFuture: " + averageExecutionTimeCompletableFuture + " ms");
            } catch (Exception e) {
                System.out.println("\nError running tests: " + e.getMessage());
            }
        });
    }

    private void runSequential(final String filePath) {
        Filters filters = new Filters(filePath);
        long startTime1 = System.nanoTime();
        filters.ConditionalBlurFilter("outputConditionalBlurFilter.png",matrixSize);
        long endTime1 = System.nanoTime();
        long time1 = endTime1 - startTime1;
        time1 = TimeUnit.NANOSECONDS.toMillis(time1);
        System.out.println("Sequential execution time1: " + time1 + " ms");

        long startTime2 = System.nanoTime();
        filters.ConditionalBlurFilter("outputConditionalBlurFilter.png",matrixSize);
        long endTime2 = System.nanoTime();
        long time2 = endTime2 - startTime2;
        time2 = TimeUnit.NANOSECONDS.toMillis(time2);
        System.out.println("Sequential execution time2: " + time2 + " ms");

        long startTime3 = System.nanoTime();
        filters.ConditionalBlurFilter("outputConditionalBlurFilter.png",matrixSize);
        long endTime3 = System.nanoTime();
        long time3 = endTime3 - startTime3;
        time3 = TimeUnit.NANOSECONDS.toMillis(time3);
        System.out.println("Sequential execution time3: " + time3 + " ms");

        System.out.println("\nAverage execution time for sequential: " + (time1 + time2 + time3) / 3 + " ms");
    }
}
