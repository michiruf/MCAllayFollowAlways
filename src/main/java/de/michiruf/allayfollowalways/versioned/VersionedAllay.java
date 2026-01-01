package de.michiruf.allayfollowalways.versioned;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;

/**
 * @author Michael Ruf
 * @since 2023-12-18
 */
public class VersionedAllay {

    public static void resetPortalCooldown(AllayEntity allay) {
        //? if <1.19.1 {
        /*allay.resetNetherPortalCooldown();
        *///? } else {
        allay.resetPortalCooldown();
        //? }
    }

    public static boolean hasPortalCooldown(AllayEntity allay) {
        //? if <1.19.1 {
        /*return allay.hasNetherPortalCooldown();
        *///? } elif =1.19.1 {
        /*return allay.hasPortalCooldownn(); // typo
        *///? } else {
        return allay.hasPortalCooldown();
        //? }
    }

    public static boolean isDancing(AllayEntity allay) {
        // In MC 1.19, allays cannot yet dance nor being duplicated

        //? if <1.19.1 {
        /*return false;
        *///? } else {
        return allay.isDancing();
        //? }
    }

    public static Entity getLeashHolder(AllayEntity allay) {
        //? if <1.21 {
        /*return allay.getHoldingEntity();
        *///? } else {
        return allay.getLeashHolder();
        //? }
    }
}
