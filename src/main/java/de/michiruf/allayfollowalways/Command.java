package de.michiruf.allayfollowalways;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Michael Ruf
 * @since 2022-12-03
 */
public class Command {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher,
                                        CommandRegistryAccess registry,
                                        CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> afaNode = CommandManager
                .literal("allayfollowalways")
                .requires(cmd -> cmd.hasPermissionLevel(4))
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("Usage: /allayfollowalways OPTION [VALUE]"));
                    context.getSource().sendMessage(Text.literal("Usage: /allayfollowalways options -> list all options"));
                    return 1;
                })
                .build();

        registerConfigOptionsCommand(afaNode);
        registerConfigCommand(afaNode, "rangeFactor", Main.CONFIG::rangeFactor, Main.CONFIG::rangeFactor, DoubleArgumentType.doubleArg(), DoubleArgumentType::getDouble);
        registerConfigCommand(afaNode, "movementSpeedFactor", Main.CONFIG::movementSpeedFactor, Main.CONFIG::movementSpeedFactor, FloatArgumentType.floatArg(), FloatArgumentType::getFloat);
        registerConfigCommand(afaNode, "teleportEnabled", Main.CONFIG::teleportEnabled, Main.CONFIG::teleportEnabled, BoolArgumentType.bool(), BoolArgumentType::getBool);
        registerConfigCommand(afaNode, "teleportDistance", Main.CONFIG::teleportDistance, Main.CONFIG::teleportDistance, FloatArgumentType.floatArg(), FloatArgumentType::getFloat);
        registerConfigCommand(afaNode, "considerEntityTeleportationCooldown", Main.CONFIG::considerEntityTeleportationCooldown, Main.CONFIG::considerEntityTeleportationCooldown, BoolArgumentType.bool(), BoolArgumentType::getBool);
        registerConfigCommand(afaNode, "avoidTeleportingIntoWater", Main.CONFIG::avoidTeleportingIntoWater, Main.CONFIG::avoidTeleportingIntoWater, BoolArgumentType.bool(), BoolArgumentType::getBool);
        registerConfigCommand(afaNode, "avoidTeleportingIntoLava", Main.CONFIG::avoidTeleportingIntoLava, Main.CONFIG::avoidTeleportingIntoLava, BoolArgumentType.bool(), BoolArgumentType::getBool);
        registerConfigCommand(afaNode, "avoidTeleportingIntoWalls", Main.CONFIG::avoidTeleportingIntoWalls, Main.CONFIG::avoidTeleportingIntoWalls, BoolArgumentType.bool(), BoolArgumentType::getBool);
        dispatcher.getRoot().addChild(afaNode);
    }

    private static void registerConfigOptionsCommand(LiteralCommandNode<ServerCommandSource> node) {
        node.addChild(CommandManager
                .literal("options")
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("logLevel [int]"));
                    context.getSource().sendMessage(Text.literal("rangeFactor [double]"));
                    context.getSource().sendMessage(Text.literal("movementSpeedFactor [float]"));
                    context.getSource().sendMessage(Text.literal("teleportEnabled [boolean]"));
                    context.getSource().sendMessage(Text.literal("teleportDistance [float]"));
                    context.getSource().sendMessage(Text.literal("considerEntityTeleportationCooldown [boolean]"));
                    context.getSource().sendMessage(Text.literal("avoidTeleportingIntoWater [boolean]"));
                    context.getSource().sendMessage(Text.literal("avoidTeleportingIntoLava [boolean]"));
                    context.getSource().sendMessage(Text.literal("avoidTeleportingIntoWalls [boolean]"));
                    return 1;
                })
                .build());
    }

    private static <T> void registerConfigCommand(LiteralCommandNode<ServerCommandSource> node, String name, Supplier<T> getter, Consumer<T> setter, ArgumentType<T> type, BiFunction<CommandContext<ServerCommandSource>, String, T> valueExtractor) {
        node.addChild(CommandManager
                .literal(name)
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal(name + " is currently set to " + getter.get()));
                    return 1;
                })
                .then(CommandManager.argument(name, type)
                        .executes(context -> {
                            setter.accept(valueExtractor.apply(context, name));
                            context.getSource().sendMessage(Text.literal(name + " was set to " + getter.get()));
                            return 1;
                        }))
                .build());

    }
}
