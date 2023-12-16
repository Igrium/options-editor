package com.igrium.options_editor.cmd;

import static net.minecraft.server.command.CommandManager.*;
import static com.igrium.options_editor.cmd.ProtoArgumentTypes.*;

import com.igrium.options_editor.core.ConfigProviderType;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ConfigCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("config").then(
            literal("edit").then(
                identifierArgument("id", ConfigProviderType.REGISTRY.getIds()).executes(
                    context -> {
                        context.getSource().sendFeedback(() -> Text.literal("Editing config: " + IdentifierArgumentType.getIdentifier(context, "id")), false);
                        return 1;
                    }
                )
            )
        ));
    }
}
