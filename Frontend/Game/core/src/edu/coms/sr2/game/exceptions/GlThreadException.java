package edu.coms.sr2.game.exceptions;

public class GlThreadException extends RuntimeException {
    public GlThreadException() {
        super("Cannot call GL-dependent tasks on non-GL thread.");
    }
    public GlThreadException(String message) {
        super(message);
    }
}
