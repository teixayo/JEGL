package me.teixayo.jegl.loop;

import me.teixayo.jegl.AsyncTestManager;
import me.teixayo.jegl.LoopAppExample;
import me.teixayo.jegl.loop.loops.Loop;
import me.teixayo.jegl.loop.loops.LoopType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoopStatsTest {

    @Test
    @Order(1)
    public void testLoopStats() {
        LoopStats loopStats = new LoopStats(20);

        assertEquals(20, loopStats.getUpdatePerSecond());
        assertEquals(20, loopStats.getCurrentUpdatePerSecond());

        for (int i = 0; i < 40; i++) {
            loopStats.update((long) 1E6, (long) 50E6);
        }
        assertEquals(20.0, loopStats.getCurrentUpdatePerSecond());
        assertEquals(1.0, loopStats.getCurrentMillisPerUpdate());
        assertEquals(40, loopStats.getUpdates());
    }

    @ParameterizedTest
    @Order(2)
    @MethodSource("me.teixayo.jegl.Statics#getArguments")
    public void testRealLoopApp(LoopType loopType, int updatePerSecond) {
        Loop loop = LoopBuilder.builder()
                .loopType(loopType)
                .updatePerSecond(updatePerSecond)
                .useThread()
                .loopApp(LoopAppExample.getINSTANCE())
                .build();

        AsyncTestManager.submitTask(() -> {
            loop.start();
            Thread.sleep(3000);
            loop.cancel();
            LoopStats loopStats = loop.getLoopStats();
            System.out.printf(
                    "Loop Type: %-15s | Update Per Second: %4d | Current Update Rate: %6.2f | Millis Per Update: %6.2f | Delta: %.2f%n",
                    loopType.name(),
                    updatePerSecond,
                    loopStats.getCurrentUpdatePerSecond(),
                    loopStats.getCurrentMillisPerUpdate(),
                    updatePerSecond / 500.0
            );

            assertEquals(updatePerSecond, loopStats.getUpdatePerSecond(), updatePerSecond / 500.0);
            return null;
        });
    }

    @Test
    @Order(3)
    public void executeLoopTests() {
        AsyncTestManager.waitForCompletion();
    }
}
