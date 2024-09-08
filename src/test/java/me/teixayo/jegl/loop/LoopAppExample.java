package me.teixayo.jegl.loop;

import lombok.Getter;
import lombok.SneakyThrows;

public class LoopAppExample implements LoopApp {

    @Getter
    private static final LoopApp INSTANCE = new LoopAppExample();


    @SneakyThrows
    @Override
    public void update() {
        Thread.sleep(3);
    }

    @Override
    public void close() {
    }

    @Override
    public void start() {
    }
}
