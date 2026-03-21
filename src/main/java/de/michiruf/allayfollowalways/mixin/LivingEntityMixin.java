package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.allay.Allay;
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
    private float speed;

    @Redirect(method = "getSpeed()F", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;speed:F", opcode = Opcodes.GETFIELD))
    private float getMovementSpeed_applyFactor(LivingEntity entity) {
        if (!(entity instanceof Allay))
            return speed;
        return speed * AllayFollowAlwaysMod.CONFIG.movementSpeedFactor();
    }
}
