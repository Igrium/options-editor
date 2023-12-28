package com.igrium.options_editor.client;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.igrium.options_editor.net.UpdateConfigC2SPacket;
import com.igrium.options_editor.options.Option;
import com.igrium.options_editor.options.OptionHolder;
import com.igrium.options_editor.options.OptionType;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClientOptionHolder {
    private final OptionHolder optionHolder;
    private final int screenId;

    public ClientOptionHolder(OptionHolder optionHolder, int screenId) {
        this.optionHolder = optionHolder;
        this.screenId = screenId;
    }

    public final OptionHolder getOptionHolder() {
        return optionHolder;
    }

    public int getScreenId() {
        return screenId;
    }

    public Screen buildScreen(@Nullable Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal(optionHolder.getName()));
        
        for (OptionHolder.OptionCategoryEntry cat : optionHolder.getCategories()) {
            ConfigCategory category = builder.getOrCreateCategory(Text.literal(cat.getName()));
            
            for (var option : cat.getCategory().getOptions()) {
                category.addEntry(buildField(builder, option.option(), option.name(), option.id()));
            }
        }
        builder.setSavingRunnable(this::saveAndClose);
        return builder.build();
    }

    private <T> AbstractConfigListEntry<?> buildField(ConfigBuilder builder, Option<T> option, String name, String id) {
        OptionType<T> type = option.getType();
        ConfigFieldType<T> fieldType = ConfigFieldType.get(type);
        if (fieldType == null) {
            throw new IllegalStateException(
                    "Config field does not have a client-side renderer: " + OptionType.REGISTRY.getId(type));
        }

        return fieldType.getField(builder, option, name, id, option::setValue);
    }

    private boolean removed;

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved() {
        this.removed = true;
    }

    public void onScreenRemoved() {
        ClientPlayNetworking.send(new UpdateConfigC2SPacket(screenId, Optional.empty()));
    }

    public void saveAndClose() {
        ClientPlayNetworking.send(new UpdateConfigC2SPacket(screenId, Optional.of(optionHolder)));
        setRemoved();
        MinecraftClient.getInstance().setScreen(null);
    }
}
