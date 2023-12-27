package com.igrium.options_editor.options;

public class OptionProviders {

    // public static final OptionProvider SERVER = OptionProvider.register("minecraft:server", new ServerOptionProvider());
    public static final OptionProvider GAMERULES = OptionProvider.register("minecraft:gamerules", null);

    // empty for classloader
    public static void registerDefaults() {

    }
}
