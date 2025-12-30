package de.michiruf.allayfollowalways;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.test.TestContext;

public class GameTestHelper {

    public static ServerCommandSource playerCommandSource(PlayerEntity player, TestContext context) {
        return player.getCommandSource(context.getWorld());
    }
}
