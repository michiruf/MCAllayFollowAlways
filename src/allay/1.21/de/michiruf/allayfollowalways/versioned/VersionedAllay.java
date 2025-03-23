package de.michiruf.allayfollowalways.versioned;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class VersionedAllay {

    public static void resetPortalCooldown(AllayEntity allay) {
        allay.resetPortalCooldown();
    }

    public static boolean hasPortalCooldown(AllayEntity allay) {
        return allay.hasPortalCooldown();
    }

    public static boolean isDancing(AllayEntity allay) {
        return allay.isDancing();
    }

    public static Entity getLeashHolder(AllayEntity allay) {
        return allay.getLeashHolder();
    }
}
