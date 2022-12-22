package de.michiruf.allayfollowalways.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Michael Ruf
 * @since 2022-12-22
 */
public class EnumSubCommand {

    public static <T extends Enum<T>> void createAndRegister(
            LiteralCommandNode<ServerCommandSource> node,
            String name,
            Supplier<T> getter,
            Consumer<T> setter,
            Class<T> enumClass) {
        // Create the parameter command
        var parameterCommand = CommandManager
                .literal(name)
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal(name + " is currently set to " + getter.get()));
                    return 1;
                });

        // Create and add subcommands for enum values
        var enumValues = enumClass.getEnumConstants();
        for (var enumValue : enumValues) {
            var enumCommand = CommandManager.literal(enumValue.name())
                    .executes(context -> {
                        setter.accept(Enum.valueOf(enumClass, enumValue.name()));
                        context.getSource().sendMessage(Text.literal(name + " was set to " + getter.get()));
                        return 1;
                    });
            parameterCommand.then(enumCommand);
        }

        // Finally, add the complete node
        node.addChild(parameterCommand.build());
    }
}
