package com.igrium.options_editor.mixin;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.igrium.options_editor.util.timer.Timer;
import com.igrium.options_editor.util.timer.TimerProvider;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.profiler.Profiler;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements TimerProvider {

    @Unique
    private Deque<Timer> timers = new ConcurrentLinkedDeque<>();

    @Shadow
    private Profiler profiler;

    @Override
    public void addTimer(Timer timer) {
        if (timer.isInitialized()) {
            throw new IllegalStateException("The supplied timer has already been initialized!");
        }
        timer.setInitialized();
        timers.add(timer);
    }

    @Override
    public Collection<Timer> getTimers() {
        return timers;
    }

    @Inject(method = "tickWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/function/CommandFunctionManager;tick()V"))
    void optionseditor$onTickWorlds(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        Iterator<Timer> iterator = timers.iterator();
        profiler.push("timers");
        while(iterator.hasNext()) {
            Timer timer = iterator.next();
            if (timer.timeLeft <= 0) {
                timer.runnable.run();
                iterator.remove();
            } else {
                timer.timeLeft--;
            }
        }
        profiler.pop();
    }
}
