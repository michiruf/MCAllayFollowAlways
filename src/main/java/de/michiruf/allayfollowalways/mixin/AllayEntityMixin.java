package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.config.LogLevel;
import de.michiruf.allayfollowalways.allay.AllayLeashBehaviour;
import de.michiruf.allayfollowalways.allay.AllayPlayerLookup;
import de.michiruf.allayfollowalways.allay.AllayTeleportBehaviour;
import de.michiruf.allayfollowalways.helper.WorldComparator;
import de.michiruf.allayfollowalways.helper.ChunkTicketHelper;
import de.michiruf.allayfollowalways.helper.EntityHelper;
import net.minecraft.world.entity.animal.allay.Allay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
@Mixin(Allay.class)
public abstract class AllayEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick_addTeleport(CallbackInfo info) {
        var allay = (Allay) (Object) this;
        AllayTeleportBehaviour.handleTeleport(allay);
    }

    @Inject(method = "shouldStayCloseToLeashHolder", at = @At("HEAD"), cancellable = true)
    private void shouldFollowLeash_setShouldFollow(CallbackInfoReturnable<Boolean> cir) {
        var allay = (Allay) (Object) this;
        var shouldFollow = AllayLeashBehaviour.shouldFollowLeash(allay);
        AllayFollowAlwaysMod.LOGGER.tick(LogLevel.DEBUG, "Leash follow override for allay {}: {}", allay.getStringUUID(), shouldFollow);
        cir.setReturnValue(shouldFollow);
        // NOTE Is this cancel necessary in this case?
        cir.cancel();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick_addChunkUnloadingDelay(CallbackInfo info) {
        var allay = (Allay) (Object) this;

        // If allay is not in a movable state, then stop
        if (!AllayTeleportBehaviour.canFollowPlayer(allay))
            return;

        // If teleportation cooldown is considered, the allay might not teleport to the player immediately, due to the
        // fact, that the teleport might be still on cooldown
        // To ensure, that the allay will get teleported later, the chunk must be kept loaded also, when the player
        // is in a different world
        var playerOptional = AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown()
                ? AllayPlayerLookup.getLikedPlayerGlobal(allay)
                : AllayPlayerLookup.getLikedPlayerForWorld(allay, EntityHelper.getLevel(allay));
        if (playerOptional.isEmpty())
            return;
        var player = playerOptional.get();

        // Fail safety check
        if (AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown() && !WorldComparator.equals(player, allay))
            return;

        // If not in survival or creative mode, skip chunk loading entirely
        if (!(player.gameMode.isSurvival() || player.isCreative()))
            return;

        AllayFollowAlwaysMod.LOGGER.tick(LogLevel.DEBUG, "Keeping chunks loaded for allay {}", allay.getStringUUID());
        ChunkTicketHelper.keepChunksLoaded(allay);
    }
}
