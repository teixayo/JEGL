package me.teixayo.jegl.loop.loops.types.busywait;

import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.loop.loops.Loop;

public class BusyWaitLoop extends Loop {

    public BusyWaitLoop(int updatePerSecond, boolean useThread, LoopApp loopApp) {
        super(updatePerSecond, useThread, loopApp);
    }

    @Override
    public void sleep() {
        long nextTick = startTime + (nanosPerUpdate * updates);
        while (System.nanoTime() < nextTick) {
        }
    }
}
