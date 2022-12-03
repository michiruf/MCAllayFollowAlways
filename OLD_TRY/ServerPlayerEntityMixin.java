package de.michiruf.allayfollowalways.OLD_TRY;

import de.michiruf.allayfollowalways.Main;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "moveToWorld", at = @At(value = "HEAD"))
    private void moveToWorld_callEvent(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        Main.LOGGER.error("PLAYER SWITCHED DIMENSIONS");
        PlayerBeforeSwitchWorldCallback.EVENT.invoker().onSwitchWorld((ServerPlayerEntity) (Object) this);
    }
}
