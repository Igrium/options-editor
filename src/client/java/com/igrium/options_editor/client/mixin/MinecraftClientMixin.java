package com.igrium.options_editor.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.igrium.options_editor.client.events.ScreenRemovedCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    Screen currentScreen;

    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;removed()V"))
    void optionseditor$onSetScreen(Screen screen, CallbackInfo ci) {
        if (currentScreen != null) {
            ScreenRemovedCallback.EVENT.invoker().onScreenRemoved((MinecraftClient) (Object) this, screen);
        }
    }
}
