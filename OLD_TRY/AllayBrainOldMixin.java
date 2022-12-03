package de.michiruf.allayfollowalways.OLD_TRY;

import net.minecraft.entity.passive.AllayBrain;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Michael Ruf
 * @since 2022-11-29
 */
@Mixin(AllayBrain.class)
public class AllayBrainOldMixin {

    @Redirect(method = "getLikedPlayer", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/World;isClient()Z"))
    private static boolean getLikedPlayer_removeClientCondition(World world) {
        // Remove the client condition for the teleport code to work while still using getLikedPlayer()
        // NOTE This might be not needed anymore
        return false;
    }
}
