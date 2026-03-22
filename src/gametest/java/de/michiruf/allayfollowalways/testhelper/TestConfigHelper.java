package de.michiruf.allayfollowalways.testhelper;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.config.LeashMode;
import de.michiruf.allayfollowalways.config.LogLevel;

/**
 * Resets all config values to their defaults before a test,
 * ensuring no leftover state from a previous test.
 */
public class TestConfigHelper {

    public static void resetToDefaults() {
        AllayFollowAlwaysMod.CONFIG.rangeFactor(1.0);
        AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(1f);
        AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
        AllayFollowAlwaysMod.CONFIG.teleportDistance(65f);
        AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown(false);
        AllayFollowAlwaysMod.CONFIG.teleportWhenDancing(true);
        AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWater(false);
        AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoLava(false);
        AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
        AllayFollowAlwaysMod.CONFIG.playerLeashMode(LeashMode.NONE);
        AllayFollowAlwaysMod.CONFIG.generalLeashMode(LeashMode.DIRECTIONAL_SLOW_DOWN);
        AllayFollowAlwaysMod.CONFIG.leashSlowDownDistanceStart(6);
        AllayFollowAlwaysMod.CONFIG.leashSlowDownDistanceEnd(8);
        AllayFollowAlwaysMod.CONFIG.leashSlowDownDegree(90);
        AllayFollowAlwaysMod.CONFIG.logMod(LogLevel.INFO);
        AllayFollowAlwaysMod.CONFIG.logTeleport(LogLevel.INFO);
        AllayFollowAlwaysMod.CONFIG.logLeash(LogLevel.INFO);
    }
}
