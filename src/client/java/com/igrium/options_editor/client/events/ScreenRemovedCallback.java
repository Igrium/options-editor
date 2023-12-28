package com.igrium.options_editor.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

/**
 * Called right before {@link Screen#removed()}
 */
public interface ScreenRemovedCallback {

    public static final Event<ScreenRemovedCallback> EVENT = EventFactory.createArrayBacked(ScreenRemovedCallback.class,
            listeners -> (client, screen) -> {
                for (var l : listeners) {
                    l.onScreenRemoved(client, screen);
                }
            });

    public void onScreenRemoved(MinecraftClient client, Screen screen);
}
