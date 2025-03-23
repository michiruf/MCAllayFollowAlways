package de.michiruf.allayfollowalways.versioned;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class VersionedAllay {

    public static void resetPortalCooldown(AllayEntity allay) {
        allay.resetNetherPortalCooldown();
    }

    public static boolean hasPortalCooldown(AllayEntity allay) {
        return allay.hasNetherPortalCooldown();
    }

    public static boolean isDancing(AllayEntity allay) {
        // In MC 1.19, allays cannot yet dance nor being duplicated
        return false;
    }

    public static Entity getLeashHolder(AllayEntity allay) {
        return allay.getHoldingEntity();
    }
}
