package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AllayEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Michael Ruf
 * @since 2022-11-29
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow
    private float movementSpeed;

    @Redirect(method = "getMovementSpeed()F", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;movementSpeed:F", opcode = Opcodes.GETFIELD))
    private float getMovementSpeed_applyFactor(LivingEntity entity) {
        if (entity instanceof AllayEntity)
            return movementSpeed * Main.CONFIG.movementSpeedFactor();
        return movementSpeed;
    }
}
