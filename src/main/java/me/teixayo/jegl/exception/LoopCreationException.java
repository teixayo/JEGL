package me.teixayo.jegl.exception;

public class LoopCreationException extends RuntimeException {

    public LoopCreationException(Throwable cause) {
        super("Error while creating the loop.", cause);
    }
}
