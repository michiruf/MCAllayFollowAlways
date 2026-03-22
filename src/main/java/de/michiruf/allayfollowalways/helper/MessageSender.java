package de.michiruf.allayfollowalways.helper;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

/**
 * @author Michael Ruf
 * @since 2023-12-13
 */
public class MessageSender {

    public static void send(CommandContext<CommandSourceStack> context, String text) {
        //? if <1.19.1 {
        /*var source = context.getSource();
        var serverPlayerEntity = source.getPlayer();
        if (serverPlayerEntity != null) {
            serverPlayerEntity.sendSystemMessage(Component.literal(text));
        } else {
            // MRU: I am not entirely sure, if sendFeedback or sendError should be the option to go here
            source.sendSuccess(Component.literal(text), false);
        }
        *///? } else {
        context.getSource().sendSystemMessage(Component.literal(text));
        //? }
    }
}
