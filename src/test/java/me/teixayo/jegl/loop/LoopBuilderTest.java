package me.teixayo.jegl.loop;

import me.teixayo.jegl.LoopAppExample;
import me.teixayo.jegl.exception.InvalidLoopConfigurationException;
import me.teixayo.jegl.loop.loops.LoopType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class LoopBuilderTest {

    @ParameterizedTest
    @MethodSource("me.teixayo.jegl.Statics#getArguments")
    public void testLoopBuilder(LoopType loopType, int updatePerSecond) {
        LoopBuilder loopBuilder = LoopBuilder.builder()
                .loopType(loopType)
                .updatePerSecond(updatePerSecond)
                .loopApp(LoopAppExample.getINSTANCE());
        assertEquals(loopType, loopBuilder.getLoopType());
        assertEquals(updatePerSecond, loopBuilder.getUpdatePerSecond());
        assertFalse(loopBuilder.isUseThread());
        loopBuilder.useThread();
        assertTrue(loopBuilder.isUseThread());
        assertEquals(LoopAppExample.getINSTANCE(), loopBuilder.getLoopApp());
        assertDoesNotThrow(loopBuilder::build);
    }

    @Test
    public void testLoopBuilderExceptions() {
        LoopBuilder loopBuilder = LoopBuilder.builder()
                .loopType(null)
                .updatePerSecond(20)
                .loopApp(LoopAppExample.getINSTANCE());
        assertThrows(InvalidLoopConfigurationException.class, loopBuilder::build);
        loopBuilder.updatePerSecond(0);
        assertThrows(InvalidLoopConfigurationException.class, loopBuilder::build);
        loopBuilder.updatePerSecond(-1);
        assertThrows(InvalidLoopConfigurationException.class, loopBuilder::build);
    }
}