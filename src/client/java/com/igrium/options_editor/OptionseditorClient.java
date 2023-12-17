package com.igrium.options_editor;

import net.fabricmc.api.ClientModInitializer;

public class OptionsEditorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigScreensNet.registerListeners();
    }
}