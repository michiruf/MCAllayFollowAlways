package de.michiruf.allayfollowalways.allay;

import net.minecraft.world.entity.animal.allay.Allay;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class AllayCompatibility {

    public static boolean isDancing(Allay allay) {
        // In MC 1.19, allays cannot yet dance nor being duplicated

        //? if <1.19.1 {
        /*return false;
        *///? } else {
        return allay.isDancing();
        //? }
    }
}
