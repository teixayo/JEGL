package me.teixayo.jegl.loop.loops;

import lombok.Getter;
import me.teixayo.jegl.exception.InvalidLoopConfigurationException;
import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.loop.LoopStats;

@Getter
public abstract class Loop implements Runnable {

    protected long nanosPerUpdate;
    protected LoopApp loopApp;
    protected boolean isRunning;
    protected long updates;
    protected long startTime;
    protected LoopStats loopStats;
    private Thread thread;

    public Loop(int updatePerSecond, boolean useThread, LoopApp loopApp) {
        this.nanosPerUpdate = (long) ((1.0 / updatePerSecond) * 1E9);
        this.isRunning = false;
        this.loopApp = loopApp;
        this.loopStats = new LoopStats(updatePerSecond);
        if (useThread) {
            thread = new Thread(this);
        }
    }

    public void cancel() {
        this.isRunning = false;
    }

    public void start() {
        isRunning = true;
        if (thread != null) {
            thread.start();
            return;
        }
        run();
    }

    @Override
    public void run() {
        startTime = System.nanoTime();
        loopApp.start();
        while (isRunning) {
            long startTimer = System.nanoTime();
            loopApp.update();
            long stopTimer = System.nanoTime();
            long updateTimeNanos = (stopTimer - startTimer);
            sleep();

            stopTimer = System.nanoTime();
            long elapsedTimeNanos = stopTimer - startTimer;
            loopStats.update(updateTimeNanos, elapsedTimeNanos);
            updates++;
        }
        loopApp.close();
    }
    public void changeUpdatePerSecond(int updatePerSecond) {
        if (updatePerSecond <= 0) {
            throw new IllegalArgumentException("Invalid updatePerSecond: " + updatePerSecond + ". It must be greater than 0.");
        }
        this.nanosPerUpdate = (long) ((1.0 / updatePerSecond) * 1E9);
        this.loopStats.setUpdatePerSecond(updatePerSecond);
    }

    protected abstract void sleep();

}
