import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class SwirlTestsReport {

    public SwirlTestsReport() {
    }

    public void runTests(final String filePath) {
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
                    filters.SwirlFilterMultiThread("outputSwirlFilterMultiThread.png", numThreads);
                    long endTime = System.nanoTime();
                    executionTimesMultiThread.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

                    startTime = System.nanoTime();
                    filters.SwirlFilterThreadPool("outputSwirlFilterThreadPool.png", numThreads);
                    endTime = System.nanoTime();
                    executionTimesThreadPool.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

                    startTime = System.nanoTime();
                    filters.SwirlFilterForkJoinPool("outputSwirlFilterForkJoinPool.png", numThreads);
                    endTime = System.nanoTime();
                    executionTimesForkJoinPool.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

                    startTime = System.nanoTime();
                    filters.SwirlFilterCompletableFuture("outputSwirlFilterCompletableFuture.png", numThreads);
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
        filters.SwirlFilter("outputSwirlFilter.png");
        long endTime1 = System.nanoTime();
        long time1 = endTime1 - startTime1;

        long startTime2 = System.nanoTime();
        filters.SwirlFilter("outputSwirlFilter.png");
        long endTime2 = System.nanoTime();
        long time2 = endTime2 - startTime2;

        long startTime3 = System.nanoTime();
        filters.SwirlFilter("outputSwirlFilter.png");
        long endTime3 = System.nanoTime();
        long time3 = endTime3 - startTime3;

        System.out.println("\nAverage execution time for sequential:" + (time1 + time2 + time3) / 3 + " ms");
    }
}