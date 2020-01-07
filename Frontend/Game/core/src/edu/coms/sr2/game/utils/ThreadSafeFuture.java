package edu.coms.sr2.game.utils;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import edu.coms.sr2.game.Threading;

public class ThreadSafeFuture<V> implements Future<V> {
    private Future<V> future;
    public ThreadSafeFuture(Future future) {
        this.future = future;
    }

    @Override
    public boolean cancel(boolean b) {
        return future.cancel(b);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public V get() {
        Threading.assertWorkerThread();
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public V get(long l, TimeUnit timeUnit) {
        Threading.assertWorkerThread();
        try {
            return future.get(l, timeUnit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
