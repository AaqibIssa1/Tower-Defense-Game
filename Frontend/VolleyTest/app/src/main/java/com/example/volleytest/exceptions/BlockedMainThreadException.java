package com.example.volleytest.exceptions;

public class BlockedMainThreadException extends RuntimeException
{
    public BlockedMainThreadException() {
        super("Cannot block on main thread.");
    }

    public BlockedMainThreadException(String message) {
        super(message);
    }
}
