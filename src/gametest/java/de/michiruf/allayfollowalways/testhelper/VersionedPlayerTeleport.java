package de.michiruf.allayfollowalways.testhelper;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class VersionedPlayerTeleport {

    public static void teleport(ServerPlayer player, Vec3 to, ServerLevel world) {
        //? if <1.21 {
        /*var target = new net.minecraft.world.level.portal.PortalInfo(
                to,
                player.getDeltaMovement(),
                player.getYRot(),
                player.getXRot()
        );
        net.fabricmc.fabric.api.dimension.v1.FabricDimensions.teleport(player, world, target);
        *///? } elif <1.21.2 {
        /*var target = new net.minecraft.world.level.portal.DimensionTransition(
                world,
                to,
                player.getDeltaMovement(),
                player.getYRot(),
                player.getXRot(),
                net.minecraft.world.level.portal.DimensionTransition.DO_NOTHING
        );
        player.changeDimension(target);
        *///? } else {
        player.teleportTo(world, to.x, to.y, to.z, java.util.Set.of(), player.getYRot(), player.getXRot(), false);
        //? }
    }
}
