package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.Main;
import de.michiruf.allayfollowalways.allay.AllayPlayerLookup;
import de.michiruf.allayfollowalways.allay.AllayTeleport;
import de.michiruf.allayfollowalways.allay.AllayTeleportBehaviour;
import de.michiruf.allayfollowalways.helper.DebugEntity;
import de.michiruf.allayfollowalways.helper.WorldComparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.VibrationListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin {

//    private boolean constructed = true;
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void constructor_callback(@SuppressWarnings("rawtypes") EntityType entityType, World world, CallbackInfo ci) {
//        // This flag gets set also, when the entity is deserialized
//        constructed = false;
//        Main.LOGGER.error("Setting constructed flag for " + DebugEntity.idString((AllayEntity) (Object) this));
//    }
//
//    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
//    public void writeCustomDataToNbt_myEvent(CallbackInfo ci) {
//        Main.LOGGER.error("writeCustomDataToNbt for " + DebugEntity.idString((AllayEntity) (Object) this));
//    }
//
//    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
//    public void readCustomDataFromNbt_myEvent(CallbackInfo ci) {
//        Main.LOGGER.error("readCustomDataFromNbt for " + DebugEntity.idString((AllayEntity) (Object) this));
//    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick_addTeleport(CallbackInfo info) {
//        // Cancel early if not initialized completely
//        if (!constructed)
//            return;
//        else
//            Main.LOGGER.error("Not constructed yet");

        var allay = (AllayEntity) (Object) this;
        AllayTeleport.handleTeleport(allay);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick_addChunkUnloadingDelay(CallbackInfo info) {
//        // Cancel early if not initialized completely
//        if (!constructed)
//            return;
//        else
//            Main.LOGGER.error("Not constructed yet");

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
        // TODO Use config flag
        cir.setReturnValue(true);
        cir.cancel();
    }
}
