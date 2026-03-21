package de.michiruf.allayfollowalways.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import de.michiruf.allayfollowalways.versioned.VersionedMessageSender;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * @author Michael Ruf
 * @since 2022-12-22
 */
public class EnumSubCommand {

    public static <T extends Enum<T>> void createAndRegister(
            LiteralCommandNode<CommandSourceStack> node,
            String name,
            Supplier<T> getter,
            Consumer<T> setter,
            Class<T> enumClass) {
        // Create the parameter command
        var parameterCommand = Commands
                .literal(name)
                .executes(context -> {
                    VersionedMessageSender.send(context, name + " is currently set to " + getter.get());
                    return 1;
                });

        // Create and add subcommands for enum values
        var enumValues = enumClass.getEnumConstants();
        for (var enumValue : enumValues) {
            var enumCommand = Commands.literal(enumValue.name())
                    .executes(context -> {
                        setter.accept(Enum.valueOf(enumClass, enumValue.name()));
                        VersionedMessageSender.send(context, name + " was set to " + getter.get());
                        return 1;
                    });
            parameterCommand.then(enumCommand);
        }

        // Finally, add the complete node
        node.addChild(parameterCommand.build());
    }
}
