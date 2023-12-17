package com.igrium.options_editor.util.timer;

import java.util.Collection;

public interface TimerProvider {
    public void addTimer(Timer timer);
    public Collection<Timer> getTimers();
}
