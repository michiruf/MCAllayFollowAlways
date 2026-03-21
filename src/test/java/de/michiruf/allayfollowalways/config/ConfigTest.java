package de.michiruf.allayfollowalways.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    @Test
    void defaultValues() {
        var config = new Config();

        // Range and movement
        assertEquals(1.0, config.rangeFactor);
        assertEquals(1f, config.movementSpeedFactor);

        // Teleport defaults
        assertTrue(config.teleportEnabled);
        assertEquals(65f, config.teleportDistance);
        assertFalse(config.considerEntityTeleportationCooldown);
        assertTrue(config.teleportWhenDancing);
        assertTrue(config.avoidTeleportingIntoWater);
        assertTrue(config.avoidTeleportingIntoLava);
        assertTrue(config.avoidTeleportingIntoWalls);

        // Leash defaults
        assertEquals(LeashMode.NONE, config.playerLeashMode);
        assertEquals(LeashMode.DIRECTIONAL_SLOW_DOWN, config.generalLeashMode);
        assertEquals(6, config.leashSlowDownDistanceStart);
        assertEquals(8, config.leashSlowDownDistanceEnd);
        assertEquals(90, config.leashSlowDownDegree);
    }

    @Test
    void fieldsAreMutable() {
        var config = new Config();

        config.teleportEnabled = false;
        assertFalse(config.teleportEnabled);

        config.teleportDistance = 100f;
        assertEquals(100f, config.teleportDistance);

        config.playerLeashMode = LeashMode.FOLLOW;
        assertEquals(LeashMode.FOLLOW, config.playerLeashMode);

        config.generalLeashMode = LeashMode.NONE;
        assertEquals(LeashMode.NONE, config.generalLeashMode);

        config.rangeFactor = 2.5;
        assertEquals(2.5, config.rangeFactor);

        config.movementSpeedFactor = 3f;
        assertEquals(3f, config.movementSpeedFactor);
    }
}
