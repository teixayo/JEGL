package me.teixayo.jegl;

import lombok.Getter;
import lombok.SneakyThrows;
import me.teixayo.jegl.loop.LoopApp;

@Getter
public class LoopAppExample implements LoopApp {
    @Getter
    private static final LoopApp INSTANCE = new LoopAppExample();
    private long totalElapsedTime;
    private long lastTime;
    private long updates;

    @SneakyThrows
    @Override
    public void update() {
        long currentTime = System.nanoTime();
        totalElapsedTime += currentTime - lastTime;
        lastTime = currentTime;
        updates++;
        Thread.sleep(3);
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
