package com.igrium.options_editor.vanilla;

import com.igrium.options_editor.options.OptionHolder;
import com.igrium.options_editor.options.OptionProvider;

import net.minecraft.server.MinecraftServer;

public class ServerOptionProvider implements OptionProvider {

    @Override
    public OptionHolder obtain(MinecraftServer server) {
        return new OptionHolder();
        // throw new UnsupportedOperationException("Unimplemented method 'obtain'");
    }

    @Override
    public void apply(OptionHolder holder, MinecraftServer server) {

    }
    
}
