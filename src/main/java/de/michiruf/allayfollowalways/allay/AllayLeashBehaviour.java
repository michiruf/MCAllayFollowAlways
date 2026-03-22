package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.config.LeashMode;
import de.michiruf.allayfollowalways.config.LogLevel;
import de.michiruf.allayfollowalways.helper.MathHelper;
import de.michiruf.allayfollowalways.helper.EntityHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.phys.Vec3;

/**
 * @author Michael Ruf
 * @since 2022-12-11
 */
public class AllayLeashBehaviour {

    public static boolean shouldFollowLeash(Allay allay) {
        var holdingEntity = allay.getLeashHolder();
        if (holdingEntity == null)
            return false;

        if (holdingEntity instanceof ServerPlayer) {
            return AllayFollowAlwaysMod.CONFIG.playerLeashMode() == LeashMode.FOLLOW;
        }

        return AllayFollowAlwaysMod.CONFIG.generalLeashMode() == LeashMode.FOLLOW;
    }

    public static Vec3 calculateLeashedVelocity(Allay allay, Vec3 velocity) {
        var holdingEntity = allay.getLeashHolder();
        if (holdingEntity == null)
            return velocity;

        if (holdingEntity instanceof ServerPlayer)
            return AllayFollowAlwaysMod.CONFIG.playerLeashMode() == LeashMode.DIRECTIONAL_SLOW_DOWN
                    ? velocity.scale(calculateDirectionalMovementFactor(allay, velocity, holdingEntity))
                    : velocity;

        return AllayFollowAlwaysMod.CONFIG.generalLeashMode() == LeashMode.DIRECTIONAL_SLOW_DOWN
                ? velocity.scale(calculateDirectionalMovementFactor(allay, velocity, holdingEntity))
                : velocity;
    }

    private static double calculateDirectionalMovementFactor(Allay allay, Vec3 allayVelocity, Entity holdingEntity) {
        // Cancel if the allay is not moving at all
        // Threshold got from Vec3d.normalize()
        if (allayVelocity.lengthSqr() <= 1.0E-4)
            return 1;

        var allayToEntity = holdingEntity.position().subtract(allay.position());
        var distance = allayToEntity.length();
        var r = Mth.inverseLerp(
                distance,
                AllayFollowAlwaysMod.CONFIG.leashSlowDownDistanceStart(),
                AllayFollowAlwaysMod.CONFIG.leashSlowDownDistanceEnd());

        // Nothing to do, because the allay is below minimum slow distance
        if (r <= 0)
            return 1;

        // Check if the allay is moving towards the center
        var angle = MathHelper.angleBetweenDeg(allayVelocity, allayToEntity, false);
        if (angle <= AllayFollowAlwaysMod.CONFIG.leashSlowDownDegree()) {
            return 1;
        }

        // Calculate the slowness by the range percentage inverse
        var factor = 1 - Mth.clamp(r, 0, 1);
        AllayFollowAlwaysMod.LOGGER.leash(LogLevel.DEBUG, "Leash slow-down for allay {}: distance={}, angle={}, factor={}",
                allay.getStringUUID(), String.format("%.2f", distance), String.format("%.1f", angle), String.format("%.2f", factor));
        return factor;
    }
}
