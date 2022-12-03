package de.michiruf.allayfollowalways.mixin;

import de.michiruf.allayfollowalways.Main;
import net.minecraft.entity.passive.AllayBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * @author Michael Ruf
 * @since 2022-11-29
 */
@Mixin(AllayBrain.class)
public class AllayBrainMixin {

    @ModifyConstant(method = "getLikedPlayer", constant = @Constant(doubleValue = 64.0))
    private static double getLikedPlayer_increaseRange(double range) {
        return range * Main.CONFIG.rangeFactor();
    }
}
