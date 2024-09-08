package me.teixayo.jegl.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class DaemonThreadTest {

    @Test
    public void testDaemonThread() {
        Thread thread = DaemonThread.active();
        assertTrue(thread.isAlive());
        assertTrue(thread.isDaemon());
    }
}