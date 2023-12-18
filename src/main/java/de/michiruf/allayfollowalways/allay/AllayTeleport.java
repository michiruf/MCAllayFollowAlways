package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.versioned.VersionedAllay;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
public class AllayTeleport {

    public static void handleTeleport(AllayEntity allay) {
        // Maybe see FollowOwnerGoal.tryTeleport()
        var o = AllayPlayerLookup.getLikedPlayer(allay);
        if (o.isEmpty())
            return;
        var player = o.get();
        handleTeleport(allay, player);
    }

    public static void handleTeleport(AllayEntity allay, ServerPlayerEntity player) {
        if (!AllayTeleportBehaviour.canFollowPlayer(allay))
            return;
        if (!AllayTeleportBehaviour.shouldTeleport(allay, player))
            return;

        // TODO Use log level
        //Main.LOGGER.info("Teleporting " + DebugEntity.idString(allay) + " to " + DebugEntity.idString(player));

        if (AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown())
            VersionedAllay.resetPortalCooldown(allay);

        // Use fabrics teleport, since it should be capable of teleporting easily through dimensions
        VersionedFabricTeleport.teleport(allay, player);
    }
}
