package edu.coms.sr2.game.exceptions;

public class LooperThreadException extends RuntimeException {
    public LooperThreadException() {
        super("Task must be called on thread that has called Looper.prepare() (Call on UI thread).");
    }
    public LooperThreadException(String message) {
        super(message);
    }
}
