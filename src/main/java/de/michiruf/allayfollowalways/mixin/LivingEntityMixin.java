package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.Main;
import de.michiruf.allayfollowalways.helper.DebugEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AllayEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Michael Ruf
 * @since 2022-11-29
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    private int fixLeashBreakingIn_1_19_tickDelayCounter = 0;
    private boolean fixLeashBreakingIn_1_19_tickDelayDone = false;

    @Shadow
    private float movementSpeed;

    @Redirect(method = "getMovementSpeed()F", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;movementSpeed:F", opcode = Opcodes.GETFIELD))
    private float getMovementSpeed_applyFactor(LivingEntity entity) {
        if (entity instanceof AllayEntity) {
            var s = movementSpeed * Main.CONFIG.movementSpeedFactor();

            // If fix is applied, temporarily return 0 movement speed in the configured amount of ticks,
            // since not moving prevents this issue
            // Since the entity is initialized not too often, this should be okay to solve like this
            if(!(fixLeashBreakingIn_1_19_tickDelayCounter > 0 && fixLeashBreakingIn_1_19_tickDelayDone))
                s = 0;

            return s;
        }
        return movementSpeed;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void fixLeashBreakingIn_1_19(CallbackInfo ci) {
        if (fixLeashBreakingIn_1_19_tickDelayDone)
            return;

        var entity = (LivingEntity) (Object)  this;
        if (!(entity instanceof AllayEntity)) {
            fixLeashBreakingIn_1_19_tickDelayDone = true;
            return;
        }

        fixLeashBreakingIn_1_19_tickDelayCounter++;
        if (fixLeashBreakingIn_1_19_tickDelayCounter <= Main.CONFIG.fixLeashBreakingIn_1_19_delay())
            return;

        fixLeashBreakingIn_1_19_tickDelayDone = true;
        Main.LOGGER.error("Fixed leash for " + DebugEntity.idString(this));
    }
}
