package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.Main;
import de.michiruf.allayfollowalways.helper.DebugEntity;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.TeleportTarget;

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

        if (Main.CONFIG.considerEntityTeleportationCooldown())
            allay.resetPortalCooldown();

        // Use fabrics teleport, since it should be capable of teleporting easily through dimensions
        FabricDimensions.teleport(allay, player.getWorld(),
                new TeleportTarget(player.getPos(), allay.getVelocity(), allay.getYaw(), allay.getPitch()));
    }
}
