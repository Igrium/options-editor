package com.igrium.options_editor.options;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public interface OptionHolderType {
    public OptionHolder load(MinecraftServer server) throws Exception;
    public void apply(MinecraftServer server, OptionHolder options) throws Exception;

    public static SimpleRegistry<OptionHolderType> REGISTRY = FabricRegistryBuilder
            .<OptionHolderType>createSimple(RegistryKey.ofRegistry(new Identifier("options-editor:option_holders")))
            .buildAndRegister();
    
    public static <T extends OptionHolderType> T register(T type, Identifier id) {
        return Registry.register(REGISTRY, id, type);
    }
}
