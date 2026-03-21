package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.allay.AllayLeashBehaviour;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.phys.Vec3;
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
    private Vec3 deltaMovement;

    @Redirect(method = "getDeltaMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;deltaMovement:Lnet/minecraft/world/phys/Vec3;", opcode = Opcodes.GETFIELD))
    private Vec3 getVelocity_applyLeashedFactor(Entity entity) {
        if (!(entity instanceof Allay allay))
            return deltaMovement;

        if (!allay.isLeashed())
            return deltaMovement;

        allay.hurtMarked = true;
        return AllayLeashBehaviour.calculateLeashedVelocity(allay, deltaMovement);
    }
}
