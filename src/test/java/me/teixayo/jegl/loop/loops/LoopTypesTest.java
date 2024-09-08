package me.teixayo.jegl.loop.loops;

import lombok.Getter;
import lombok.SneakyThrows;
import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.utils.DaemonThread;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoopTypesTest implements LoopApp {

    @Getter
    private static final int[] updatePerSecondTests = new int[]{20, 60, 120, 200};
    private static final int numberOfTests = 5;
    private static final ExecutorService executor = Executors.newFixedThreadPool(LoopType.values().length * updatePerSecondTests.length);
    private static final List<Callable<Void>> taskList = new ArrayList<>();
    private long totalElapsedTime;
    private long lastTime;
    private long updates;

    public static Stream<Arguments> arguments() {
        List<Arguments> argumentList = new ArrayList<>();
        LoopType[] loopTypes = LoopType.values();
        for (LoopType loopType : loopTypes) {
            for (int updatePerSecondTest : updatePerSecondTests) {
                for (int i = 0; i < numberOfTests; i++) {
                    argumentList.add(Arguments.of(loopType, updatePerSecondTest));
                }
            }
        }
        return Stream.of(argumentList.toArray(new Arguments[0]));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("arguments")
    @Order(1)
    public void sleepMethodTest(LoopType loopType, int updatePerSecond) {
        taskList.add(() -> {
            LoopTypesTest loopApp = new LoopTypesTest();
            Loop loop = loopType.create(updatePerSecond, true, loopApp);
            loop.start();
            Thread.sleep(1000);
            long startTime = loop.getStartTime();
            loop.cancel();
            long endTime = System.nanoTime();
            long sleepTime = endTime - startTime;
            long totalElapsedTime = loopApp.totalElapsedTime + loop.getNanosPerUpdate();

            long diff = totalElapsedTime - sleepTime;

            long expectedUpdates = totalElapsedTime / loop.getNanosPerUpdate();
            long currentUpdates = updates;
            System.out.println(sleepTime + " | " + updatePerSecond + " | diff: " + diff + " | dt:" + loop.getNanosPerUpdate());
            assertEquals(sleepTime, totalElapsedTime, loop.getNanosPerUpdate());
            assertEquals(expectedUpdates, currentUpdates, loop.getNanosPerUpdate());
            return null;
        });
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void executeLoopTests() {

        DaemonThread.active();
        for (Callable<Void> task : taskList) {
            task.call();
        }
    }

    @Override
    public void update() {
        long currentTime = System.nanoTime();
        totalElapsedTime += currentTime - lastTime;
        lastTime = currentTime;
        updates++;
    }

    @Override
    public void close() {
        long currentTime = System.nanoTime();
        totalElapsedTime += currentTime - lastTime;
    }

    @Override
    public void start() {
        lastTime = System.nanoTime();
    }
}