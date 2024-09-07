package me.teixayo.jegl.loop;

import lombok.SneakyThrows;
import me.teixayo.jegl.loop.loops.Loop;
import me.teixayo.jegl.loop.loops.LoopType;
import me.teixayo.jegl.loop.loops.LoopTypesTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoopStatsTest {
    private static final ExecutorService executor = Executors.newFixedThreadPool(LoopType.values().length * LoopTypesTest.getUpdatePerSecondTests().length);
    private static final List<Callable<Void>> taskList = new ArrayList<>();

    @Test
    @Order(1)
    public void testLoopStats() {
        LoopStats loopStats = new LoopStats(20);
        assertEquals(20, loopStats.getUpdatePerSecond());
        assertEquals(20,loopStats.getCurrentUpdatePerSecond());
        for(int i = 0; i < 40; i++) {
            loopStats.update(1_000_000L, 50_000_000);
        }
        assertEquals(loopStats.getCurrentUpdatePerSecond(), 20.0);
        assertEquals(loopStats.getCurrentMilliPerUpdate(), 1.0);
        assertEquals(loopStats.getUpdates(), 40);
    }
    @SneakyThrows
    @ParameterizedTest
    @Order(2)
    @MethodSource("me.teixayo.jegl.loop.LoopTest#arguments")
    public void testRealLoopApp(LoopType loopType, int updatePerSecond) {
        taskList.add(() -> {
            Loop loop = loopType.create(updatePerSecond, true, LoopAppExamble.getINSTANCE());
            loop.start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loop.cancel();

            LoopStats loopStats = loop.getLoopStats();

            assertEquals(updatePerSecond, loopStats.getUpdatePerSecond());

            System.out.println(loopType.name() + " " + updatePerSecond + ": " + loopStats.getCurrentUpdatePerSecond() + " | " + loopStats.getCurrentMilliPerUpdate() + " dt: " + updatePerSecond / 500.0f);
            assertEquals(updatePerSecond, loopStats.getCurrentUpdatePerSecond(), updatePerSecond / 500.0f);
            assertEquals(1.0, loopStats.getCurrentMilliPerUpdate(), 0.2);
            return null;
        });
    }

    @SneakyThrows
    @Test
    @Order(3)
    public void executeLoopTests() {
        for (Callable<Void> task : taskList) {
            task.call();
        }
    }
}