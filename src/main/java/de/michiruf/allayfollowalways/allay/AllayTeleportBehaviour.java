package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.helper.WorldComparator;
import de.michiruf.allayfollowalways.versioned.EntityHelper;
import de.michiruf.allayfollowalways.versioned.VersionedAllay;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
public class AllayTeleportBehaviour {

    public static boolean canFollowPlayer(Allay allay) {
        if (allay.isLeashed())
            return false;

        // TODO Use log level
        //Main.LOGGER.error(DebugEntity.idString(allay) + " is not leashed");

        var optionalNoteblock = allay.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
        if (optionalNoteblock != null && optionalNoteblock.isPresent())
            return false;

        return true;
    }

    public static boolean shouldTeleport(Allay allay, ServerPlayer player) {
        if (!AllayFollowAlwaysMod.CONFIG.teleportEnabled())
            return false;

        // Never teleport to a dead player
        if (!player.isAlive())
            return false;

        // Might do not follow if dancing
        if (!AllayFollowAlwaysMod.CONFIG.teleportWhenDancing() && VersionedAllay.isDancing(allay))
            return false;

        // Avoid teleporting into water
        if (AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWater() && player.isInWater())
            return false;

        // Avoid teleporting into lava
        if (AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoLava() && player.isInLava())
            return false;

        // Avoid teleporting into walls
        if (AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls() && player.isInWall())
            return false;

        // If not in the same world, we want to teleport always
        if (!WorldComparator.equals(allay, player)) {
            // To avoid teleporting entities instantly out of the nether after pushing them in,
            // do not teleport when a portal cooldown is set
            if (AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown() && VersionedAllay.hasPortalCooldown(allay))
                return false;

            return true;
        }

        // Check the teleportation distance
        var distance = EntityHelper.getPos(allay).subtract(EntityHelper.getPos(player)).length();
        return distance > AllayFollowAlwaysMod.CONFIG.teleportDistance();
    }
}
