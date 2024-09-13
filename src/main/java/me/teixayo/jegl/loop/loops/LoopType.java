package me.teixayo.jegl.loop.loops;

import lombok.Getter;
import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.loop.loops.types.lock.BusyWaitLockLoop;
import me.teixayo.jegl.loop.loops.types.busywait.BusyWaitLoop;
import me.teixayo.jegl.loop.loops.types.busywait.BusyWaitYieldLoop;
import me.teixayo.jegl.loop.loops.types.lock.LockLoop;

@Getter
public enum LoopType {
    BUSY_WAIT(BusyWaitLoop.class),
    BUSY_WAIT_YIELD(BusyWaitYieldLoop.class),
    LOCK(LockLoop.class),
    BUSY_WAIT_LOCK(BusyWaitLockLoop.class);

    private final Class<? extends Loop> loopClass;

    LoopType(Class<? extends Loop> loopClass) {
        this.loopClass = loopClass;
    }

    public Loop create(int updatePerSecond, boolean useThread, LoopApp loopApp) {
        try {
            return loopClass.getDeclaredConstructor(int.class, boolean.class, LoopApp.class)
                    .newInstance(updatePerSecond, useThread, loopApp);
        } catch (Exception exception) {
            throw new RuntimeException("Error while creating the loop.",exception);
        }
    }
}
