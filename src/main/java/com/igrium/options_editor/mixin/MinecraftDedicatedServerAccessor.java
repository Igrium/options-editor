package com.igrium.options_editor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.ServerPropertiesLoader;

@Mixin(MinecraftDedicatedServer.class)
public interface MinecraftDedicatedServerAccessor {
    @Accessor("propertiesLoader")
    public ServerPropertiesLoader getPropertiesLoader();
}
