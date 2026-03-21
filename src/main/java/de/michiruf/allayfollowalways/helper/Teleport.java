package de.michiruf.allayfollowalways.helper;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.phys.Vec3;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class Teleport {

    public static void teleport(Allay allay, ServerPlayer player) {
        teleport(allay, player, EntityHelper.getServerLevel(player));
    }

    public static void teleport(Entity entity, Entity to, ServerLevel world) {
        teleport(entity, to.position(), world);
    }

    public static void teleport(Entity entity, Vec3 to, ServerLevel world) {
        //? if <1.21 {
        /*var target = new net.minecraft.world.level.portal.PortalInfo(
                to,
                entity.getDeltaMovement(),
                entity.getYRot(),
                entity.getXRot()
        );
        net.fabricmc.fabric.api.dimension.v1.FabricDimensions.teleport(entity, world, target);
        *///? } elif <1.21.2 {
        /*var target = new net.minecraft.world.level.portal.DimensionTransition(
                world,
                to,
                entity.getDeltaMovement(),
                entity.getYRot(),
                entity.getXRot(),
                net.minecraft.world.level.portal.DimensionTransition.DO_NOTHING
        );
        entity.changeDimension(target);
        *///? } else {
        var target = new net.minecraft.world.level.portal.TeleportTransition(
                world,
                to,
                entity.getDeltaMovement(),
                entity.getYRot(),
                entity.getXRot(),
                net.minecraft.world.level.portal.TeleportTransition.DO_NOTHING
        );
        entity.teleport(target);
        //? }
    }
}
