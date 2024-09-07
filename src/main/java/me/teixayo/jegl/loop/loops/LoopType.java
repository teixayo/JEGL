package me.teixayo.jegl.loop.loops;

import lombok.Getter;
import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.loop.loops.lock.BusyWaitLockLoop;
import me.teixayo.jegl.loop.loops.busywait.BusyWaitLoop;
import me.teixayo.jegl.loop.loops.busywait.BusyWaitYield;
import me.teixayo.jegl.loop.loops.lock.LockLoop;

@Getter
public enum LoopType {
    BUSY_WAIT(BusyWaitLoop.class),
    BUSY_WAIT_YIELD(BusyWaitYield.class),
    LOCK(LockLoop.class),
    BUSY_WAIT_LOCK(BusyWaitLockLoop.class);

    private final Class<? extends Loop> loopClass;

    LoopType(Class<? extends Loop> loopClass) {
        this.loopClass = loopClass;
    }

    public Loop create(int updatePerSecond, boolean useThread, LoopApp loopApp) {
        return switch (this) {
            case BUSY_WAIT -> new BusyWaitLoop(updatePerSecond, true, loopApp);
            case BUSY_WAIT_YIELD -> new BusyWaitYield(updatePerSecond, true, loopApp);
            case BUSY_WAIT_LOCK -> new BusyWaitLockLoop(updatePerSecond, true, loopApp);
            case LOCK -> new LockLoop(updatePerSecond, true, loopApp);
        };

    }
}
