package me.teixayo.jegl.loop;

import lombok.Getter;
import me.teixayo.jegl.exception.InvalidLoopConfigurationException;
import me.teixayo.jegl.loop.loops.Loop;
import me.teixayo.jegl.loop.loops.LoopType;

@Getter
public class LoopBuilder {

    private LoopType loopType = LoopType.BUSY_WAIT_LOCK;
    private int updatePerSecond;
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
        checkLoopApp();
        checkUpdatePerSecondRange();
        checkLoopType();
        return loopType.create(updatePerSecond, useThread, loopApp);
    }

    private void checkLoopApp() {
        if (loopApp == null) {
            throw new InvalidLoopConfigurationException("The LoopApp instance is missing. Please provide a valid LoopApp.");
        }
    }

    private void checkUpdatePerSecondRange() {
        if (updatePerSecond <= 0) {
            throw new InvalidLoopConfigurationException("Invalid updatePerSecond: " + updatePerSecond + ". It must be greater than 0.");
        }
    }

    private void checkLoopType() {
        if (this.loopType == null) {
            throw new InvalidLoopConfigurationException("The LoopType is missing. Please provide a valid LoopType.");
        }
    }

}
