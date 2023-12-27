package com.igrium.options_editor.options;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

/**
 * A standard interface to retrieve and apply options on the server.
 */
public interface OptionProvider {
    /**
     * Retrieve the current option config from the server.
     * @param server Server to use.
     * @return Option holder with all relevent options.
     */
    public OptionHolder load(MinecraftServer server);

    /**
     * Apply the values from an option holder to the server.
     * @param server Server to apply to.
     * @param holder Option holder. Omitted values remain unchanged.
     */
    public void apply(MinecraftServer server, OptionHolder holder);

    public static final SimpleRegistry<OptionProvider> REGISTRY = FabricRegistryBuilder
            .<OptionProvider>createSimple(RegistryKey.ofRegistry(new Identifier("options-editor:option_providers")))
            .buildAndRegister();
    
    public static <T extends OptionProvider> T register(T value, Identifier id) {
        return Registry.register(REGISTRY, id, value);
    }
}
