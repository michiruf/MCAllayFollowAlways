package de.michiruf.allayfollowalways.versioned;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.allay.Allay;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class VersionedAllay {

    public static void resetPortalCooldown(Allay allay) {
        allay.setPortalCooldown();
    }

    public static boolean hasPortalCooldown(Allay allay) {
        return allay.isOnPortalCooldown();
    }

    public static boolean isDancing(Allay allay) {
        // In MC 1.19, allays cannot yet dance nor being duplicated

        //? if <1.19.1 {
        /*return false;
        *///? } else {
        return allay.isDancing();
        //? }
    }

    public static Entity getLeashHolder(Allay allay) {
        return allay.getLeashHolder();
    }
}
