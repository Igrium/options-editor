package com.igrium.options_editor.util.timer;

public class Timer {
    public int timeLeft;
    public final Runnable runnable;

    private boolean initialized;

    public Timer(int time, Runnable runnable) {
        if (time < 1) {
            throw new IllegalArgumentException("Time must be at least 1.");
        }
        this.timeLeft = time;
        this.runnable = runnable;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized() {
        this.initialized = true;
    }
}
