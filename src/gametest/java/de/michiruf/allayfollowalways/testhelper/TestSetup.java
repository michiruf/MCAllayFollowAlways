package de.michiruf.allayfollowalways.testhelper;

import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class TestSetup {

    public static Pair<FakePlayer, AllayEntity> createPlayerAndAllay(TestContext context) {
        var player = FakePlayer.get(context.getWorld());
        player.changeGameMode(GameMode.SURVIVAL);

        // Register player in the entity registry, necessary for lookups with uuid
        context.getWorld().onPlayerConnected(player);

        var allay = context.spawnEntity(EntityType.ALLAY, new BlockPos(0, 1, 0));

        // Teleport player to allay, because the player is not spawned in the test space
        VersionedFabricTeleport.teleport(player, allay, context.getWorld());

        // Fake that allay got an item from the player
        allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());

        return new Pair<>(player, allay);
    }
}
