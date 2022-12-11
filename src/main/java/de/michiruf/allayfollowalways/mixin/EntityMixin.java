package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.allay.AllayLeashBehaviour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Michael Ruf
 * @since 2022-12-11
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    private Vec3d velocity;

    @Redirect(method = "getVelocity", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;velocity:Lnet/minecraft/util/math/Vec3d;", opcode = Opcodes.GETFIELD))
    private Vec3d getVelocity_applyLeashedFactor(Entity entity) {
        if (!(entity instanceof AllayEntity allay))
            return velocity;

        if (!allay.isLeashed())
            return velocity;

        allay.velocityDirty = true;
        return AllayLeashBehaviour.calculateLeashedVelocity(allay, velocity);
    }
}
