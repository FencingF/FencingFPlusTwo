package org.fenci.fencingfplus2.util.client;

public class Timer {
    private long current;

    public Timer() {
        this.current = System.currentTimeMillis();
    }

    public boolean hasReached(final long delay) {
        return System.currentTimeMillis() - this.current >= delay;
    }

    public void reset() {
        this.current = System.currentTimeMillis();
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - this.current;
    }

    public long time() {
        return System.currentTimeMillis() - current;
    }
}
