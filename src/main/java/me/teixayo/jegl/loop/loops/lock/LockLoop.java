package me.teixayo.jegl.loop.loops.lock;

import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.loop.loops.Loop;

import java.util.concurrent.locks.LockSupport;

public class LockLoop extends Loop {

    public LockLoop(int updatePerSecond, boolean useThread, LoopApp loopApp) {
        super(updatePerSecond, useThread, loopApp);
    }

    @Override
    public void sleep() {
        long nextTick = startTime + (nanosPerUpdate * updates);
        while (System.nanoTime() < nextTick) {
            LockSupport.parkNanos(850000);
        }
    }
}
