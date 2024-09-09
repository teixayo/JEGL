package me.teixayo.jegl.loop.loops;

import lombok.SneakyThrows;
import me.teixayo.jegl.AsyncTestManager;
import me.teixayo.jegl.LoopAppExample;
import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.utils.DaemonThread;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoopTypesTest {

    private static final int NUMBER_OF_TESTS = 5;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("me.teixayo.jegl.Statics#getArguments")
    @Order(1)
    public void sleepMethodTest(LoopType loopType, int updatePerSecond) {
        AsyncTestManager.submitTask(NUMBER_OF_TESTS, () -> {
            LoopAppExample loopApp = new LoopAppExample();
            Loop loop = loopType.create(updatePerSecond, true, loopApp);

            loop.start();
            Thread.sleep(1000);
            long startTime = loop.getStartTime();
            loop.cancel();
            long endTime = System.nanoTime();
            long sleepTime = endTime - startTime;
            long totalElapsedTime = loopApp.getTotalElapsedTime() + loop.getNanosPerUpdate();
            long diff = totalElapsedTime - sleepTime;

            long expectedUpdates = totalElapsedTime / loop.getNanosPerUpdate();
            long currentUpdates = loopApp.getUpdates();

            System.out.printf(
                    "Loop Type: %-15s | Update Per Second: %4d | Sleep Time: %12d ns | Total Elapsed Time: %12d ns | " +
                            "Difference: %12d ns | Expected Updates: %4d | Current Updates: %4d%n",
                    loopType.name(),
                    updatePerSecond,
                    sleepTime,
                    totalElapsedTime,
                    diff,
                    expectedUpdates,
                    currentUpdates
            );

            assertEquals(totalElapsedTime, sleepTime, loop.getNanosPerUpdate());
            assertEquals(expectedUpdates, currentUpdates, loop.getNanosPerUpdate());

            return null;
        });
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void executeLoopTests() {
        AsyncTestManager.waitForCompletion();
    }
}
