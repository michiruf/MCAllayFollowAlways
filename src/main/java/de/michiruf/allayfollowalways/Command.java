package de.michiruf.allayfollowalways;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.michiruf.allayfollowalways.config.LeashMode;
import de.michiruf.allayfollowalways.helper.EnumArgumentTypeHelper;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Michael Ruf
 * @since 2022-12-03
 */
public class Command {

    private static final Map<String, Supplier<?>> commands = new HashMap<>();

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> afaNode = CommandManager
                .literal("allayfollowalways")
                .requires(cmd -> cmd.hasPermissionLevel(4))
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("Usage: /allayfollowalways OPTION [VALUE]"));
                    context.getSource().sendMessage(Text.literal("Usage: /allayfollowalways options -> list all options"));
                    return 1;
                })
                .build();

        registerConfigCommandDouble(afaNode, "rangeFactor",
                AllayFollowAlwaysMod.CONFIG::rangeFactor,
                AllayFollowAlwaysMod.CONFIG::rangeFactor);
        registerConfigCommandFloat(afaNode, "movementSpeedFactor",
                AllayFollowAlwaysMod.CONFIG::movementSpeedFactor,
                AllayFollowAlwaysMod.CONFIG::movementSpeedFactor);
        registerConfigCommandBool(afaNode, "teleportEnabled",
                AllayFollowAlwaysMod.CONFIG::teleportEnabled,
                AllayFollowAlwaysMod.CONFIG::teleportEnabled);
        registerConfigCommandFloat(afaNode, "teleportDistance",
                AllayFollowAlwaysMod.CONFIG::teleportDistance,
                AllayFollowAlwaysMod.CONFIG::teleportDistance);
        registerConfigCommandBool(afaNode, "considerEntityTeleportationCooldown",
                AllayFollowAlwaysMod.CONFIG::considerEntityTeleportationCooldown,
                AllayFollowAlwaysMod.CONFIG::considerEntityTeleportationCooldown);
        registerConfigCommandBool(afaNode, "teleportWhenDancing",
                AllayFollowAlwaysMod.CONFIG::teleportWhenDancing,
                AllayFollowAlwaysMod.CONFIG::teleportWhenDancing);
        registerConfigCommandBool(afaNode, "avoidTeleportingIntoWater",
                AllayFollowAlwaysMod.CONFIG::avoidTeleportingIntoWater,
                AllayFollowAlwaysMod.CONFIG::avoidTeleportingIntoWater);
        registerConfigCommandBool(afaNode, "avoidTeleportingIntoLava",
                AllayFollowAlwaysMod.CONFIG::avoidTeleportingIntoLava,
                AllayFollowAlwaysMod.CONFIG::avoidTeleportingIntoLava);
        registerConfigCommandBool(afaNode, "avoidTeleportingIntoWalls",
                AllayFollowAlwaysMod.CONFIG::avoidTeleportingIntoWalls,
                AllayFollowAlwaysMod.CONFIG::avoidTeleportingIntoWalls);
        registerConfigCommandEnum(afaNode, "playerLeashMode", LeashMode.class,
                AllayFollowAlwaysMod.CONFIG::playerLeashMode,
                AllayFollowAlwaysMod.CONFIG::playerLeashMode);
        registerConfigCommandEnum(afaNode, "generalLeashMode", LeashMode.class,
                AllayFollowAlwaysMod.CONFIG::generalLeashMode,
                AllayFollowAlwaysMod.CONFIG::generalLeashMode);
        registerConfigCommandDouble(afaNode, "leashSlowDownDistanceStart",
                AllayFollowAlwaysMod.CONFIG::leashSlowDownDistanceStart,
                AllayFollowAlwaysMod.CONFIG::leashSlowDownDistanceStart);
        registerConfigCommandDouble(afaNode, "leashSlowDownDistanceEnd",
                AllayFollowAlwaysMod.CONFIG::leashSlowDownDistanceEnd,
                AllayFollowAlwaysMod.CONFIG::leashSlowDownDistanceEnd);
        registerConfigCommandFloat(afaNode, "leashSlowDownDegree",
                AllayFollowAlwaysMod.CONFIG::leashSlowDownDegree,
                AllayFollowAlwaysMod.CONFIG::leashSlowDownDegree);
        registerConfigOptionsCommand(afaNode);
        dispatcher.getRoot().addChild(afaNode);
    }

    private static void registerConfigOptionsCommand(LiteralCommandNode<ServerCommandSource> node) {
        node.addChild(CommandManager
                .literal("options")
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("OPTIONS FOR ALLAY FOLLOW ALWAYS"));
                    context.getSource().sendMessage(Text.literal("============================="));
                    commands.forEach((s, supplier) -> context.getSource().sendMessage(Text.literal(s + " -> " + supplier.get())));
                    return 1;
                })
                .build());
    }

    private static void registerConfigCommandBool(LiteralCommandNode<ServerCommandSource> node, String name, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        commands.put(name + " [bool]", getter);
        registerConfigCommand(node, name, getter, setter, BoolArgumentType.bool(), BoolArgumentType::getBool);
    }

    private static void registerConfigCommandInt(LiteralCommandNode<ServerCommandSource> node, String name, Supplier<Integer> getter, Consumer<Integer> setter) {
        commands.put(name + " [int]", getter);
        registerConfigCommand(node, name, getter, setter, IntegerArgumentType.integer(), IntegerArgumentType::getInteger);
    }

    private static void registerConfigCommandFloat(LiteralCommandNode<ServerCommandSource> node, String name, Supplier<Float> getter, Consumer<Float> setter) {
        commands.put(name + " [float]", getter);
        registerConfigCommand(node, name, getter, setter, FloatArgumentType.floatArg(), FloatArgumentType::getFloat);
    }

    private static void registerConfigCommandDouble(LiteralCommandNode<ServerCommandSource> node, String name, Supplier<Double> getter, Consumer<Double> setter) {
        commands.put(name + " [double]", getter);
        registerConfigCommand(node, name, getter, setter, DoubleArgumentType.doubleArg(), DoubleArgumentType::getDouble);
    }

    private static <T extends Enum<T>> void registerConfigCommandEnum(LiteralCommandNode<ServerCommandSource> node, String name, Class<T> clazz, Supplier<T> getter, Consumer<T> setter) {
        commands.put(name + " [enum " + clazz.getSimpleName() + "]", getter);
        var e = EnumArgumentTypeHelper.create(clazz);
        // This cast should be safe, due to the fact that the enum T extends Enum<T>
        //noinspection unchecked
        registerConfigCommand(node, name, getter, setter, (ArgumentType<T>) e, e::get);
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
