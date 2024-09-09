package me.teixayo.jegl;

import me.teixayo.jegl.utils.DaemonThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class AsyncTestManager {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private static final List<Future<?>> futures = new ArrayList<>();

    static {
        DaemonThread.active();
    }

    public static void submitTask(Callable<?> task){
        Future<?> future = executorService.submit(task);
        futures.add(future);
    }
    public static void submitTask(int repeats, Callable<?> task) {
        for (int i = 0; i < repeats; i++) {
            Future<?> future = executorService.submit(task);
            futures.add(future);
        }
    }

    public static void waitForCompletion() {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException("Task execution failed", e.getCause());
            }
        }
        futures.clear();
    }
}
