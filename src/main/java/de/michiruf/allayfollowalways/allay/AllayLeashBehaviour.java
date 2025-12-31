package de.michiruf.allayfollowalways.allay;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.config.LeashMode;
import de.michiruf.allayfollowalways.helper.MyMathHelper;
import de.michiruf.allayfollowalways.versioned.EntityHelper;
import de.michiruf.allayfollowalways.versioned.VersionedAllay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author Michael Ruf
 * @since 2022-12-11
 */
public class AllayLeashBehaviour {

    public static boolean shouldFollowLeash(AllayEntity allay) {
        var holdingEntity = VersionedAllay.getLeashHolder(allay);
        if (holdingEntity == null)
            return false;

        if (holdingEntity instanceof ServerPlayerEntity) {
            return AllayFollowAlwaysMod.CONFIG.playerLeashMode() == LeashMode.FOLLOW;
        }

        return AllayFollowAlwaysMod.CONFIG.generalLeashMode() == LeashMode.FOLLOW;
    }

    public static Vec3d calculateLeashedVelocity(AllayEntity allay, Vec3d velocity) {
        var holdingEntity = VersionedAllay.getLeashHolder(allay);
        if (holdingEntity == null)
            return velocity;

        if (holdingEntity instanceof ServerPlayerEntity)
            return AllayFollowAlwaysMod.CONFIG.playerLeashMode() == LeashMode.DIRECTIONAL_SLOW_DOWN
                    ? velocity.multiply(calculateDirectionalMovementFactor(allay, velocity, holdingEntity))
                    : velocity;

        return AllayFollowAlwaysMod.CONFIG.generalLeashMode() == LeashMode.DIRECTIONAL_SLOW_DOWN
                ? velocity.multiply(calculateDirectionalMovementFactor(allay, velocity, holdingEntity))
                : velocity;
    }

    private static double calculateDirectionalMovementFactor(AllayEntity allay, Vec3d allayVelocity, Entity holdingEntity) {
        // Cancel if the allay is not moving at all
        // Threshold got from Vec3d.normalize()
        if (allayVelocity.lengthSquared() <= 1.0E-4)
            return 1;

        var allayToEntity = EntityHelper.getPos(holdingEntity).subtract(EntityHelper.getPos(allay));
        var distance = allayToEntity.length();
        var r = MathHelper.getLerpProgress(
                distance,
                AllayFollowAlwaysMod.CONFIG.leashSlowDownDistanceStart(),
                AllayFollowAlwaysMod.CONFIG.leashSlowDownDistanceEnd());

        // Nothing to do, because the allay is below minimum slow distance
        if (r <= 0)
            return 1;

        // Check if the allay is moving towards the center
        var angle = MyMathHelper.angleBetweenDeg(allayVelocity, allayToEntity, false);
        if (angle <= AllayFollowAlwaysMod.CONFIG.leashSlowDownDegree()) {
            return 1;
        }

        // Calculate the slowness by the range percentage inverse
        return 1 - MathHelper.clamp(r, 0, 1);
    }
}
