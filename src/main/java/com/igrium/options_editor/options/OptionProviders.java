package com.igrium.options_editor.options;

import com.igrium.options_editor.vanilla.ServerOptionProvider;

public class OptionProviders {

    public static final OptionProvider SERVER = OptionProvider.register("minecraft:server", new ServerOptionProvider());

    // empty for classloader
    public static void registerDefaults() {

    }
}
