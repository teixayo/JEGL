package me.teixayo.jegl.loop;

import lombok.SneakyThrows;
import me.teixayo.jegl.loop.loops.Loop;
import me.teixayo.jegl.loop.loops.LoopType;
import me.teixayo.jegl.utils.DaemonThread;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoopTest {
    private static final int[] updatePerSecondTests = new int[]{20, 60, 120, 300, 500};
    private static final ExecutorService executor = Executors.newFixedThreadPool(LoopType.values().length * updatePerSecondTests.length);
    private static final List<Callable<Void>> taskList = new ArrayList<>();

    public static Stream<Arguments> arguments() {


        List<Arguments> argumentList = new ArrayList<>();
        for (LoopType loopType : LoopType.values()) {
            for (int updatePerSecondTest : updatePerSecondTests) {
                argumentList.add(Arguments.of(loopType, updatePerSecondTest));
            }
        }
        return Stream.of(argumentList.toArray(new Arguments[0]));
    }


    @SneakyThrows
    @ParameterizedTest
    @Order(1)
    @MethodSource("arguments")
    public void testLoopCycle(LoopType loopType, int updatePerSecond) {
        LoopBuilder loopBuilder = LoopBuilder.builder()
                .loopType(loopType)
                .updatePerSecond(updatePerSecond)
                .useThread()
                .loopApp(LoopAppExample.getINSTANCE());
        Loop loop = loopBuilder.build();
        Assertions.assertFalse(loop.isRunning());
        taskList.add(() -> {
            loop.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assertions.assertEquals(updatePerSecond, loop.getUpdates(),2);
            Assertions.assertTrue(loop.isRunning());
            loop.cancel();
            Assertions.assertFalse(loop.isRunning());
            System.out.println(updatePerSecond + " | " + loop.getUpdates() + " " + loopType.name() + " passed.");
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
}
