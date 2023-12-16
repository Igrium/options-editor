package com.igrium.options_editor.cmd;

import java.util.Collection;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class ProtoArgumentTypes {
    public static RequiredArgumentBuilder<ServerCommandSource, Identifier> identifierArgument(String name, Collection<Identifier> collection) {
        return CommandManager.argument(name, IdentifierArgumentType.identifier()).suggests(CollectionSuggestionProvider.toStringProvider(collection));
    }
}
