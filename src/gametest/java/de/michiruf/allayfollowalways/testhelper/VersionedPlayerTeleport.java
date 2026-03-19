package de.michiruf.allayfollowalways.testhelper;

//? if >= 1.19.4 {
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
//? }

public class VersionedPlayerTeleport {

    //? if >= 1.19.4 {
    public static void teleport(ServerPlayerEntity player, Vec3d to, ServerWorld world) {
        //? if <1.21 {
        /*var target = new net.minecraft.world.TeleportTarget(
                to,
                player.getVelocity(),
                player.getYaw(),
                player.getPitch()
        );
        net.fabricmc.fabric.api.dimension.v1.FabricDimensions.teleport(player, world, target);
        *///? } elif <1.21.2 {
        /*var target = new net.minecraft.world.TeleportTarget(
                world,
                to,
                player.getVelocity(),
                player.getYaw(),
                player.getPitch(),
                net.minecraft.world.TeleportTarget.NO_OP
        );
        player.teleportTo(target);
        *///? } else {
        player.teleport(world, to.x, to.y, to.z, java.util.Set.of(), player.getYaw(), player.getPitch(), false);
        //? }
    }
    //? }
}
