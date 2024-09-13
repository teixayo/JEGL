package me.teixayo.jegl.utils;
public class DaemonThread {

    private static Thread daemonThread;

    public static Thread active() {
        if (daemonThread == null) {
            daemonThread = new Thread(() -> {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            daemonThread.setDaemon(true);
            daemonThread.start();
        }
        return daemonThread;
    }
}
