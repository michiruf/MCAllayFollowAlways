package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.Main;
import de.michiruf.allayfollowalways.allay.AllayPlayerLookup;
import de.michiruf.allayfollowalways.allay.AllayTeleport;
import de.michiruf.allayfollowalways.allay.AllayTeleportBehaviour;
import de.michiruf.allayfollowalways.helper.WorldComparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick_addTeleport(CallbackInfo info) {
        var allay = (AllayEntity) (Object) this;
        AllayTeleport.handleTeleport(allay);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick_addChunkUnloadingDelay(CallbackInfo info) {
        var allay = (AllayEntity) (Object) this;

        // If allay is not in a movable state, then stop
        if (!AllayTeleportBehaviour.canFollowPlayer(allay))
            return;

        // If teleportation cooldown is considered, the allay might not teleport to the player immediately, due to the
        // fact, that the teleport might is still on cooldown
        // To ensure, that the allay will get teleported later, the chunk must get loaded also, when the player
        // is in a different world
        var playerOptional = Main.CONFIG.considerEntityTeleportationCooldown()
                ? AllayPlayerLookup.getLikedPlayerGlobal(allay)
                : AllayPlayerLookup.getLikedPlayerForWorld(allay, allay.getWorld());
        if (playerOptional.isEmpty())
            return;
        var player = playerOptional.get();

        // Only for fail safety, getLikedPlayerForWorld should only return the player in the same world
        if (Main.CONFIG.considerEntityTeleportationCooldown() && !WorldComparator.equals(player.getWorld(), allay.getWorld()))
            return;

        // If not in survival or creative mode, skip chunk loading entirely
        if (!(player.interactionManager.isSurvivalLike() || player.isCreative()))
            return;

        keepChunkLoaded(allay);
    }

    private static void keepChunkLoaded(Entity entity) {
        // Keep the chunk loaded for 2 ticks
        var cm = (ServerChunkManager) entity.getWorld().getChunkManager();
        cm.addTicket(
                ChunkTicketType.create("allayfollowalways", Comparator.comparingLong(ChunkPos::toLong), 1),
                entity.getChunkPos(),
                2,
                entity.getChunkPos());

        // NOTE This variant here might not be the most performant thing ever, since we queue another chunk ticket
        //      on every tick, but for now, this should at least work pretty well
    }

    @Inject(method = "shouldFollowLeash", at = @At("HEAD"), cancellable = true)
    private void fixLeashBreakingIn_1_19(CallbackInfoReturnable<Boolean> cir) {
        if (!Main.CONFIG.fixLeashBreakingIn_1_19_followLeash())
            return;
        cir.setReturnValue(true);
        cir.cancel();
    }
}
