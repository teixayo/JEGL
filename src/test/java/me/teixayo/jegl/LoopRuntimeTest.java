package me.teixayo.jegl;

import com.sun.management.OperatingSystemMXBean;
import lombok.SneakyThrows;
import me.teixayo.jegl.loop.LoopApp;
import me.teixayo.jegl.loop.LoopBuilder;
import me.teixayo.jegl.loop.loops.Loop;
import me.teixayo.jegl.loop.loops.LoopType;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class LoopRuntimeTest implements LoopApp {

    private final Loop loop;
    private final DecimalFormat format = new DecimalFormat("0.000");

    private static final OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static double getCPUUsage() {
        return operatingSystemMXBean.getProcessCpuLoad();
    }

    public LoopRuntimeTest() {
        this.loop = LoopBuilder.builder()
                .loopApp(this)
                .loopType(LoopType.BUSY_WAIT_LOCK)
                .useThread()
                .updatePerSecond(20)
                .build();
        this.loop.start();
    }


    public static void main(String[] args) {
        new LoopRuntimeTest();
    }

    @Override
    public void start() {
    }

    @SneakyThrows
    @Override
    public void update() {
        if (loop.getLoopStats().getUpdates() % 20 == 0) {
            System.out.println("TPS: " + format.format(loop.getLoopStats().getCurrentUpdatePerSecond()) +
                    " | CPU " + format.format(getCPUUsage() * 100.0) + "%" +
                    " | MSPT " + format.format(loop.getLoopStats().getCurrentMilliPerUpdate()));
        }
    }

    public void close() {
    }

}
