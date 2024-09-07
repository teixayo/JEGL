package me.teixayo.jegl.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class DaemonThreadTest {
    @BeforeAll
    public static void testDaemonThread() {
        Thread thread = DaemonThread.active();
        assertTrue(thread.isAlive());
        assertTrue(thread.isDaemon());
    }
}