package de.michiruf.allayfollowalways;

import io.wispforest.owo.config.annotation.Config;

/**
 * @author Michael Ruf
 * @since 2022-12-02
 */
@Config(name = "allayfollowalways", wrapperName = "Config")
@SuppressWarnings("unused")
public class ConfigModel {

    public int logLevel; // TODO Use this properly
    public double rangeFactor = 1.0;
    public float movementSpeedFactor = 1f;
    public boolean teleportEnabled = true;
    public float teleportDistance = 65f;
    public boolean considerEntityTeleportationCooldown = false;
    public boolean avoidTeleportingIntoWater = true;
    public boolean avoidTeleportingIntoLava = true;
    public boolean avoidTeleportingIntoWalls = true;
}
