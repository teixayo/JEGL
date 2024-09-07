package me.teixayo.jegl.loop;

import me.teixayo.jegl.loop.loops.Loop;
import me.teixayo.jegl.loop.loops.LoopType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class LoopBuilderTest {

    private static boolean useThread = false;

    @ParameterizedTest
    @MethodSource("me.teixayo.jegl.loop.LoopTest#arguments")
    public void testLoopBuilder(LoopType loopType, int updatePerSecond) {
        useThread = !useThread;
        LoopBuilder loopBuilder = LoopBuilder.builder()
                .loopType(loopType)
                .updatePerSecond(updatePerSecond)
                .loopApp(LoopAppExamble.getINSTANCE());
        if (useThread) loopBuilder.useThread();

        Assertions.assertEquals(loopBuilder.getLoopType(), loopType);
        Assertions.assertEquals(loopBuilder.getUpdatePerSecond(), updatePerSecond);
        Assertions.assertEquals(loopBuilder.isUseThread(), useThread);
        Assertions.assertEquals(loopBuilder.getLoopApp(), LoopAppExamble.getINSTANCE());
        Loop loop = loopBuilder.build();
        Assertions.assertNotNull(loop);
        System.out.println(updatePerSecond + " " + loopType.name() + " passed.");
    }

}