package me.teixayo.jegl.loop;

import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;

public class LoopAppExamble implements LoopApp {

    @Getter
    private static final LoopApp INSTANCE = new LoopAppExamble();


    @SneakyThrows
    @Override
    public void update() {
        Assertions.assertTrue(Thread.currentThread().isAlive());
        Thread.sleep(1);
    }

    @Override
    public void close() {
        Assertions.assertTrue(Thread.currentThread().isInterrupted());
    }

    @Override
    public void start() {
        Assertions.assertTrue(Thread.currentThread().isAlive());
    }
}
