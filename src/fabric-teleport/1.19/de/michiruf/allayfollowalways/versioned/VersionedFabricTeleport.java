package de.michiruf.allayfollowalways.versioned;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class VersionedFabricTeleport {

    public static void teleport(AllayEntity allay, ServerPlayerEntity player) {
        teleport(allay, player, player.getWorld());
    }

    public static void teleport(Entity entity, Entity to, ServerWorld world) {
        teleport(entity, to.getPos(), world);
    }

    public static void teleport(Entity entity, Vec3d to, ServerWorld world) {
        var target = new TeleportTarget(
                to,
                entity.getVelocity(),
                entity.getYaw(),
                entity.getPitch()
        );
        FabricDimensions.teleport(entity, world, target);
    }
}
