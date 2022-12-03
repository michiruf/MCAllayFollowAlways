package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.Main;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * @author Michael Ruf
 * @since 2022-12-01
 */
public class AllayPlayerLookup {
    public static Optional<ServerPlayerEntity> getLikedPlayer(AllayEntity allay) {
        // See original functionality in AllayBrain.getLikedPlayer(allay)
        var player = getLikedPlayerForWorld(allay, allay.getWorld());
        if (player.isEmpty())
            player = getLikedPlayerGlobal(allay);
        return player;
    }

    public static Optional<ServerPlayerEntity> getLikedPlayerForWorld(AllayEntity allay, World world) {
        if (world instanceof ServerWorld serverWorld) {
            var optional = allay.getBrain().getOptionalMemory(MemoryModuleType.LIKED_PLAYER);
            if (optional.isPresent()) {
                var player = (ServerPlayerEntity) serverWorld.getEntity(optional.get());
                if (player != null && (player.interactionManager.isSurvivalLike() || player.interactionManager.isCreative()))
                    return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public static Optional<ServerPlayerEntity> getLikedPlayerGlobal(AllayEntity allay) {
        var server = allay.getWorld().getServer();
        if (server == null) {
            Main.LOGGER.error("Could not get server from allay entity");
            return Optional.empty();
        }
        var worlds = server.getWorlds();
        if (worlds == null) {
            Main.LOGGER.error("Could not get worlds from allay entity");
            return Optional.empty();
        }
        for (var world : worlds) {
            var player = getLikedPlayerForWorld(allay, world);
            if (player.isPresent())
                return player;
        }
        return Optional.empty();
    }
}
