package de.michiruf.allayfollowalways.config;

import io.wispforest.owo.config.annotation.Config;

/**
 * @author Michael Ruf
 * @since 2022-12-02
 */
@Config(name = "allayfollowalways", wrapperName = "Config")
@SuppressWarnings("unused")
public class ConfigModel {

    public int logLevel; // TODO Use this
    public double rangeFactor = 1.0;
    public float movementSpeedFactor = 1f;

    /*
     * Teleportation section
     */
    public boolean teleportEnabled = true;
    public float teleportDistance = 65f;
    public boolean considerEntityTeleportationCooldown = false;
    public boolean teleportWhenDancing = true;
    public boolean avoidTeleportingIntoWater = true;
    public boolean avoidTeleportingIntoLava = true;
    public boolean avoidTeleportingIntoWalls = true;

    /*
     * Leash section
     */
    public LeashMode playerLeashMode = LeashMode.NONE;
    public LeashMode generalLeashMode = LeashMode.DIRECTIONAL_SLOW_DOWN;
    public double leashSlowDownDistanceStart = 6;
    public double leashSlowDownDistanceEnd = 8;
    public float leashSlowDownDegree = 90;
}
