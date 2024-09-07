package me.teixayo.jegl.loop.loops.lock;

import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.loop.loops.Loop;

import java.util.concurrent.locks.LockSupport;

public class BusyWaitLockLoop extends Loop {

    public BusyWaitLockLoop(int updatePerSecond, boolean useThread, LoopApp loopApp) {
        super(updatePerSecond, useThread, loopApp);
    }

    @Override
    public void sleep() {
        long nextTick = startTime + (nanosPerUpdate * updates) - 850_000;
        while (System.nanoTime() < nextTick) {
            LockSupport.parkNanos(850_000);
        }
        while (System.nanoTime() < nextTick + 850_000) {
            Thread.yield();
        }
    }
}
