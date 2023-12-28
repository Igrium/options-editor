package com.igrium.options_editor.options;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public interface OptionProvider {

    /**
     * Obtain the option values from the game and put them in an option holder.
     * @return New option holder.
     */
    public OptionHolder obtain(MinecraftServer server);

    /**
     * Read the values in an option holder and apply them to the game.
     * @param holder Option holder to read.
     */
    public void apply(OptionHolder holder, MinecraftServer server);

    public static final Registry<OptionProvider> REGISTRY = FabricRegistryBuilder
            .<OptionProvider>createSimple(RegistryKey.ofRegistry(new Identifier("options-editor:option_providers")))
            .buildAndRegister();
    
    public static <T extends OptionProvider> T register(Identifier id, T provider) {
        return Registry.register(REGISTRY, id, provider);
    }
    
    public static <T extends OptionProvider> T register(String id, T provider) {
        return register(new Identifier(id), provider);
    }
}
