package com.igrium.options_editor.options;

import com.igrium.options_editor.vanilla.GamerulesOptionProvider;
import com.igrium.options_editor.vanilla.ServerOptionProvider;

public class OptionProviders {

    public static final OptionProvider SERVER = OptionProvider.register("minecraft:server", new ServerOptionProvider());
    public static final OptionProvider GAMERULES = OptionProvider.register("minecraft:gamerules", new GamerulesOptionProvider());

    // empty for classloader
    public static void registerDefaults() {

    }
}
