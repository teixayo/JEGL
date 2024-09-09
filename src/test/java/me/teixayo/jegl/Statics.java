package me.teixayo.jegl;

import me.teixayo.jegl.loop.loops.LoopType;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Statics {

    private static final int[] UPDATE_PER_SECOND_TESTS = {20, 60, 120, 200};
    public static Stream<Arguments> getArguments() {
        List<Arguments> argumentList = new ArrayList<>();
        for (LoopType loopType : LoopType.values()) {
            for (int updatePerSecond : UPDATE_PER_SECOND_TESTS) {
                argumentList.add(Arguments.of(loopType, updatePerSecond));
            }
        }
        return argumentList.stream();
    }
}
