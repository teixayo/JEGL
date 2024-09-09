package me.teixayo.jegl.loop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoopStats {

    private int updatePerSecond;
    private double currentMillisPerUpdate;
    private double currentUpdatePerSecond;
    private long totalElapsedTimeNanos;
    private int updates;


    public LoopStats(int updatePerSecond) {
        this.updatePerSecond = updatePerSecond;
        this.currentUpdatePerSecond = updatePerSecond;
        this.currentMillisPerUpdate = 0.0f;
    }

    public void update(long updateTimeNanos, long elapsedTimeNanos) {
        currentMillisPerUpdate = updateTimeNanos / 1E6;
        totalElapsedTimeNanos += elapsedTimeNanos;
        updates++;
        if (updates % updatePerSecond == 0) {
            currentUpdatePerSecond = 1.0f / (totalElapsedTimeNanos / (updatePerSecond * 1E9));
            totalElapsedTimeNanos = 0;
        }
    }
}
