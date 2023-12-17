package com.igrium.options_editor.cmd;

import static net.minecraft.server.command.CommandManager.*;

import static com.igrium.options_editor.cmd.ProtoArgumentTypes.*;

import com.igrium.options_editor.ClientConfigInterface;
import com.igrium.options_editor.ClientConfigInterface.ScreenOpenedResponse;
import com.igrium.options_editor.core.ConfigProvider;
import com.igrium.options_editor.core.ConfigProviders;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.logging.LogUtils;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfigCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("config").requires(source -> source.hasPermissionLevel(3)).then(
            literal("edit").then(
                identifierArgument("id", ConfigProviders.REGISTRY.getIds()).executes(ConfigCommand::editConfig)
            )
        ));
    }

    private static DynamicCommandExceptionType UNKNOWN_CONFIG = new DynamicCommandExceptionType(id -> Text.literal("Unknown config type: " + id));

    public static int editConfig(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        Identifier configType = IdentifierArgumentType.getIdentifier(context, "id");

        ConfigProvider<?> provider = ConfigProviders.REGISTRY.get(configType);
        if (provider == null) {
            throw UNKNOWN_CONFIG.create(configType);
        }

        try {
            ClientConfigInterface.getInstance(player.getServer()).openConfigScreen(player, provider).thenAccept(res -> {
                if (res == ScreenOpenedResponse.SUCCESS) {
                    context.getSource().sendFeedback(() -> Text.literal("Opened config screen on client."), false);
                } else if (res == ScreenOpenedResponse.FAILED) {
                    context.getSource()
                            .sendError(Text.literal("Unable to open config screen due to a client-side error."));
                } else if (res == ScreenOpenedResponse.NO_RESPONSE) {
                    context.getSource().sendError(Text.literal(
                            "The client did not respond to the config screen request. It probably does not have this mod installed."));
                }
            });
        } catch (Exception e) {
            LogUtils.getLogger().error("Error opening config screen", e);
        }

        return 1;
    }
}
