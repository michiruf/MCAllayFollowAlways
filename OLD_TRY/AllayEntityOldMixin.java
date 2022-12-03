package de.michiruf.allayfollowalways.OLD_TRY;

import de.michiruf.allayfollowalways.AllayPlayerLookup;
import de.michiruf.allayfollowalways.AllayTeleport;
import de.michiruf.allayfollowalways.Main;
import de.michiruf.allayfollowalways.helper.DebugEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
@Mixin(AllayEntity.class)
public class AllayEntityOldMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor_addEvent(EntityType<? extends AllayEntity> entityType, World world, CallbackInfo ci) {
        PlayerBeforeSwitchWorldCallback.EVENT.register(this::onPlayerSwitchWorld);
    }

    private void onPlayerSwitchWorld(ServerPlayerEntity player) {
        // TODO If to switch config
        if (true)
            return;

        var allay = (AllayEntity) (Object) this;
        var optionalPlayerForAllay = AllayPlayerLookup.getLikedPlayer(allay);
        if (optionalPlayerForAllay.isEmpty())
            return;
        var playerForAllay = optionalPlayerForAllay.get();

        Main.LOGGER.error(DebugEntity.idString(player) + " =!= " + DebugEntity.idString(playerForAllay));
        if (player == playerForAllay || playerForAllay.getUuid().equals(player.getUuid())) {
            allay.resetPortalCooldown();
            AllayTeleport.handleTeleport(allay, player);
            Main.LOGGER.error("THOSE ARE EQUAL");
        } else
            Main.LOGGER.error("THOSE ARE NOT EQUAL");
    }
}
