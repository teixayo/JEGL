package me.teixayo.jegl.loop;

import lombok.SneakyThrows;
import me.teixayo.jegl.AsyncTestManager;
import me.teixayo.jegl.LoopAppExample;
import me.teixayo.jegl.loop.loops.Loop;
import me.teixayo.jegl.loop.loops.LoopType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoopTest {

    @SneakyThrows
    @ParameterizedTest
    @Order(1)
    @MethodSource("me.teixayo.jegl.Statics#getArguments")
    public void testLoopCycle(LoopType loopType, int updatePerSecond) {
        LoopBuilder loopBuilder = LoopBuilder.builder()
                .loopType(loopType)
                .updatePerSecond(updatePerSecond)
                .useThread()
                .loopApp(LoopAppExample.getINSTANCE());
        Loop loop = loopBuilder.build();

        assertFalse(loop.isRunning());
        AsyncTestManager.submitTask(() -> {
                    loop.start();
                    Thread.sleep(1000);
            assertEquals(updatePerSecond, loop.getUpdates(),2);
            assertTrue(loop.isRunning());
            loop.cancel();
            assertFalse(loop.isRunning());
            System.out.printf(
                    "Loop Type: %-15s | Expected Updates: %4d | Actual Updates: %4d | Status: Passed%n",
                    loopType.name(),
                    updatePerSecond,
                    loop.getUpdates()
            );
            return null;
        });
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void executeLoopTests() {
        AsyncTestManager.waitForCompletion();
    }

    @Test
    public void testLoopChangeUpdatePerSecond() {
        LoopBuilder loopBuilder = LoopBuilder.builder()
                .updatePerSecond(20)
                .useThread()
                .loopApp(LoopAppExample.getINSTANCE());
        Loop loop = loopBuilder.build();
        loop.changeUpdatePerSecond(60);
        assertThrows(IllegalArgumentException.class,() -> loop.changeUpdatePerSecond(0));
        assertEquals((long) ((1.0 / 60) * 1E9), loop.getNanosPerUpdate());
        assertEquals(60.0, loop.getLoopStats().getUpdatePerSecond());
    }
}
