package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.helper.WorldComparator;
import de.michiruf.allayfollowalways.helper.EntityHelper;
import de.michiruf.allayfollowalways.helper.Teleport;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
public class AllayTeleportBehaviour {

    public static void handleTeleport(Allay allay) {
        var o = AllayPlayerLookup.getLikedPlayer(allay);

        if (o.isEmpty())
            return;

        handleTeleport(allay, o.get());
    }

    public static void handleTeleport(Allay allay, ServerPlayer player) {
        if (!AllayTeleportBehaviour.canFollowPlayer(allay))
            return;
        if (!AllayTeleportBehaviour.shouldTeleport(allay, player))
            return;

        if (AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown())
            allay.setPortalCooldown();

        // Use fabrics teleport, since it should be capable of teleporting easily through dimensions
        Teleport.teleport(allay, player);
    }

    public static boolean canFollowPlayer(Allay allay) {
        if (allay.isLeashed())
            return false;

        var optionalNoteblock = allay.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
        if (optionalNoteblock.isPresent())
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
        if (!AllayFollowAlwaysMod.CONFIG.teleportWhenDancing() && AllayCompatibility.isDancing(allay))
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
            return !AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown()
                    || !allay.isOnPortalCooldown();
        }

        // Check the teleportation distance
        var distanceSqrt = allay.position().subtract(player.position()).lengthSqr();
        return distanceSqrt > AllayFollowAlwaysMod.CONFIG.teleportDistanceSqrt();
    }
}
