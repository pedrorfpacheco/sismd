import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class GlassTestsReport {

    public GlassTestsReport() {
    }

    public void runTests(final String filePath) throws InterruptedException {
        runSequential(filePath);
        IntStream.rangeClosed(2, 16).forEach(numThreads -> {
            try {
                List<Long> executionTimesMultiThread = new ArrayList<>();
                List<Long> executionTimesThreadPool = new ArrayList<>();
                List<Long> executionTimesForkJoinPool = new ArrayList<>();
                List<Long> executionTimesCompletableFuture = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    Filters filters = new Filters(filePath);

                    long startTime = System.nanoTime();
                    filters.GlassFilterMultiThread("outputGlassFilterMultiThread.png", numThreads);
                    long endTime = System.nanoTime();
                    executionTimesMultiThread.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

                    filters = new Filters(filePath);
                    startTime = System.nanoTime();
                    filters.GlassFilterThreadPool("outputGlassFilterThreadPool.png", numThreads);
                    endTime = System.nanoTime();
                    executionTimesThreadPool.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

                    filters = new Filters(filePath);
                    startTime = System.nanoTime();
                    filters.GlassFilterForkJoinPool("outputGlassFilterForkJoinPool.png", numThreads);
                    endTime = System.nanoTime();
                    executionTimesForkJoinPool.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

                    filters = new Filters(filePath);
                    startTime = System.nanoTime();
                    filters.GlassFilterCompletableFuture("outputGlassFilterCompletableFuture.png", numThreads);
                    endTime = System.nanoTime();
                    executionTimesCompletableFuture.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
                }

                double averageExecutionTimeMultiThread = executionTimesMultiThread.stream().mapToLong(val -> val).average().orElse(0.0);
                double averageExecutionTimeThreadPool = executionTimesThreadPool.stream().mapToLong(val -> val).average().orElse(0.0);
                double averageExecutionTimeForkJoinPool = executionTimesForkJoinPool.stream().mapToLong(val -> val).average().orElse(0.0);
                double averageExecutionTimeCompletableFuture = executionTimesCompletableFuture.stream().mapToLong(val -> val).average().orElse(0.0);

                System.out.println("\nAverage execution time for " + numThreads + " threads:");
                System.out.println("MultiThread: " + averageExecutionTimeMultiThread + " ms");
                System.out.println("ThreadPool: " + averageExecutionTimeThreadPool + " ms");
                System.out.println("ForkJoinPool: " + averageExecutionTimeForkJoinPool + " ms");
                System.out.println("CompletableFuture: " + averageExecutionTimeCompletableFuture + " ms");
            } catch (Exception e) {
                System.out.println("\nError running tests: " + e.getMessage());
            }
        });
    }

    private void runSequential(final String filePath) {
        Filters filters = new Filters(filePath);
        long startTime1 = System.nanoTime();
        filters.GlassFilter("outputGlassFilter.png");
        long endTime1 = System.nanoTime();
        long time1 = endTime1 - startTime1;

        long startTime2 = System.nanoTime();
        filters.GlassFilter("outputGlassFilter.png");
        long endTime2 = System.nanoTime();
        long time2 = endTime2 - startTime2;

        long startTime3 = System.nanoTime();
        filters.GlassFilter("outputGlassFilter.png");
        long endTime3 = System.nanoTime();
        long time3 = endTime3 - startTime3;

        System.out.println("\nAverage execution time for sequential:" + (time1 + time2 + time3) / 3 + " ms");
    }
}