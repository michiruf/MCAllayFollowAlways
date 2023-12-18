package de.michiruf.allayfollowalways.versioned;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.TeleportTarget;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class VersionedFabricTeleport {

    public static void teleport(AllayEntity allay, ServerPlayerEntity player) {
        FabricDimensions.teleport(allay, player.getServerWorld(),
                new TeleportTarget(player.getPos(), allay.getVelocity(), allay.getYaw(), allay.getPitch()));
    }
}
