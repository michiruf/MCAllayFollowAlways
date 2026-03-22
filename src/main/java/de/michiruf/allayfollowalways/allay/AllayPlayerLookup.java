package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.config.LogLevel;
import de.michiruf.allayfollowalways.helper.EntityHelper;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.Level;

/**
 * @author Michael Ruf
 * @since 2022-12-01
 */
public class AllayPlayerLookup {
    public static Optional<ServerPlayer> getLikedPlayer(Allay allay) {
        // See original functionality in AllayBrain.getLikedPlayer(allay)
        var player = getLikedPlayerForWorld(allay, EntityHelper.getLevel(allay));
        if (player.isEmpty())
            player = getLikedPlayerGlobal(allay);
        if (player.isEmpty())
            AllayFollowAlwaysMod.LOGGER.teleport(LogLevel.DEBUG, "Allay {} has no liked player", allay.getStringUUID());
        return player;
    }

    public static Optional<ServerPlayer> getLikedPlayerForWorld(Allay allay, Level world) {
        if (world instanceof ServerLevel serverWorld) {
            var optional = allay.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
            if (optional.isPresent()) {
                var player = (ServerPlayer) serverWorld.getEntity(optional.get());
                if (player != null && (player.gameMode.isSurvival() || player.gameMode.isCreative()))
                    return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public static Optional<ServerPlayer> getLikedPlayerGlobal(Allay allay) {
        var server = EntityHelper.getLevel(allay).getServer();
        if (server == null) {
            AllayFollowAlwaysMod.LOGGER.error("Could not get server from allay entity");
            return Optional.empty();
        }
        var worlds = server.getAllLevels();
        if (worlds == null) {
            AllayFollowAlwaysMod.LOGGER.error("Could not get worlds from allay entity");
            return Optional.empty();
        }
        for (var world : worlds) {
            var player = getLikedPlayerForWorld(allay, world);
            if (player.isPresent()) {
                AllayFollowAlwaysMod.LOGGER.teleport(LogLevel.INFO, "Found liked player for allay {} in different dimension {}",
                        allay.getStringUUID(), world.dimension());
                return player;
            }
        }
        return Optional.empty();
    }
}
