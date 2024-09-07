package me.teixayo.jegl.loop;

import lombok.Getter;
import me.teixayo.jegl.loop.loops.Loop;
import me.teixayo.jegl.loop.loops.LoopType;

@Getter
public class LoopBuilder {

    private LoopType loopType = LoopType.LOCK;
    private int updatePerSecond = 60;
    private boolean useThread = false;
    private LoopApp loopApp;

    public static LoopBuilder builder() {
        return new LoopBuilder();
    }

    public LoopBuilder loopType(LoopType loopType) {
        this.loopType = loopType;
        return this;
    }

    public LoopBuilder updatePerSecond(int updatePerSecond) {
        this.updatePerSecond = updatePerSecond;
        return this;
    }

    public LoopBuilder useThread() {
        useThread = true;
        return this;
    }

    public LoopBuilder loopApp(LoopApp loopApp) {
        this.loopApp = loopApp;
        return this;
    }

    public Loop build() {
       return loopType.create(updatePerSecond, useThread, loopApp);
    }

}
