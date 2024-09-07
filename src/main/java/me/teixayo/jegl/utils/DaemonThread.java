package me.teixayo.jegl.utils;

public class DaemonThread {

    public static Thread active() {
        Thread deamonThread = new Thread(() -> {
            try {

                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        deamonThread.setDaemon(true);
        deamonThread.start();
        return deamonThread;
    }
}
