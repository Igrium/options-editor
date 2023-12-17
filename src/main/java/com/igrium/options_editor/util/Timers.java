package com.igrium.options_editor.util;

import java.util.concurrent.CompletableFuture;

import net.minecraft.server.MinecraftServer;

public class Timers {
    public static void addTimer(MinecraftServer server, Timer timer) {
        ((TimerProvider) server).addTimer(timer);
    }

    public static CompletableFuture<Void> timeout(TimerProvider timerProvider, int ticks) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        timerProvider.addTimer(new Timer(ticks, () -> future.complete(null)));
        return future;
    }

    public static CompletableFuture<Void> timeout(MinecraftServer server, int ticks) {
        return timeout((TimerProvider) server, ticks);
    }
}
