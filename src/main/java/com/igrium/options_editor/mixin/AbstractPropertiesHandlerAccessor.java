package com.igrium.options_editor.mixin;

import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.dedicated.AbstractPropertiesHandler;

@Mixin(AbstractPropertiesHandler.class)
public interface AbstractPropertiesHandlerAccessor {
    @Accessor("properties")
    public Properties getProperties();
}
