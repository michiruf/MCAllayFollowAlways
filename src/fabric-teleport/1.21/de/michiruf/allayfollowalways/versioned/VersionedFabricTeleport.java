package de.michiruf.allayfollowalways.versioned;

import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.TeleportTarget;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class VersionedFabricTeleport {

    public static void teleport(AllayEntity allay, ServerPlayerEntity player) {
        var target = new TeleportTarget(
                player.getServerWorld(),
                player.getPos(),
                allay.getVelocity(),
                allay.getYaw(),
                allay.getPitch(),
                TeleportTarget.NO_OP
        );
        allay.teleportTo(target);
    }
}
