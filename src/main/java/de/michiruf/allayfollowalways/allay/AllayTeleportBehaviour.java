package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.Main;
import de.michiruf.allayfollowalways.helper.DebugEntity;
import de.michiruf.allayfollowalways.helper.WorldComparator;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
public class AllayTeleportBehaviour {

    public static boolean canFollowPlayer(AllayEntity allay) {
        if (allay.isLeashed())
            return false;

        Main.LOGGER.error(DebugEntity.idString(allay) + " is not leashed");

        var optionalNoteblock = allay.getBrain().getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK);
        if (optionalNoteblock.isPresent())
            return false;

        return true;
    }

    public static boolean shouldTeleport(AllayEntity allay, ServerPlayerEntity player) {
        if (!Main.CONFIG.teleportEnabled())
            return false;

        // Never teleport to a dead player
        if (!player.isAlive())
            return false;

        // Might do not follow if dancing
        if(!Main.CONFIG.teleportWhenDancing() && allay.isDancing())
            return false;

        // Avoid teleporting into water
        if (Main.CONFIG.avoidTeleportingIntoWater() && player.isTouchingWater())
            return false;

        // Avoid teleporting into lava
        if (Main.CONFIG.avoidTeleportingIntoLava() && player.isInLava())
            return false;

        // Avoid teleporting into walls
        if (Main.CONFIG.avoidTeleportingIntoWalls() && player.isInsideWall())
            return false;

        // If not in the same world, we want to teleport always
        if (!WorldComparator.equals(allay.getWorld(), player.getWorld())) {
            // To avoid teleporting entities instantly out of the nether after pushing them in,
            // do not teleport when a portal cooldown is set
            if (Main.CONFIG.considerEntityTeleportationCooldown() && allay.hasPortalCooldown())
                return false;

            return true;
        }

        // Check the teleportation distance
        var distance = allay.getPos().subtract(player.getPos()).length();
        return distance > Main.CONFIG.teleportDistance();
    }
}
