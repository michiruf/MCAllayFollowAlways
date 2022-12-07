package de.michiruf.allayfollowalways;

import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Config extends ConfigWrapper<de.michiruf.allayfollowalways.ConfigModel> {

    private final Option<java.lang.Integer> logLevel = this.optionForKey(new Option.Key("logLevel"));
    private final Option<java.lang.Double> rangeFactor = this.optionForKey(new Option.Key("rangeFactor"));
    private final Option<java.lang.Float> movementSpeedFactor = this.optionForKey(new Option.Key("movementSpeedFactor"));
    private final Option<java.lang.Boolean> teleportEnabled = this.optionForKey(new Option.Key("teleportEnabled"));
    private final Option<java.lang.Float> teleportDistance = this.optionForKey(new Option.Key("teleportDistance"));
    private final Option<java.lang.Boolean> considerEntityTeleportationCooldown = this.optionForKey(new Option.Key("considerEntityTeleportationCooldown"));
    private final Option<java.lang.Boolean> teleportWhenDancing = this.optionForKey(new Option.Key("teleportWhenDancing"));
    private final Option<java.lang.Boolean> avoidTeleportingIntoWater = this.optionForKey(new Option.Key("avoidTeleportingIntoWater"));
    private final Option<java.lang.Boolean> avoidTeleportingIntoLava = this.optionForKey(new Option.Key("avoidTeleportingIntoLava"));
    private final Option<java.lang.Boolean> avoidTeleportingIntoWalls = this.optionForKey(new Option.Key("avoidTeleportingIntoWalls"));
    private final Option<java.lang.Boolean> fixLeashBreakingIn_1_19 = this.optionForKey(new Option.Key("fixLeashBreakingIn_1_19"));
    private final Option<java.lang.Integer> fixLeashBreakingIn_1_19_delay = this.optionForKey(new Option.Key("fixLeashBreakingIn_1_19_delay"));

    private Config() {
        super(de.michiruf.allayfollowalways.ConfigModel.class);
    }

    public static Config createAndLoad() {
        var wrapper = new Config();
        wrapper.load();
        return wrapper;
    }

    public int logLevel() {
        return logLevel.value();
    }

    public void logLevel(int value) {
        logLevel.set(value);
    }

    public double rangeFactor() {
        return rangeFactor.value();
    }

    public void rangeFactor(double value) {
        rangeFactor.set(value);
    }

    public float movementSpeedFactor() {
        return movementSpeedFactor.value();
    }

    public void movementSpeedFactor(float value) {
        movementSpeedFactor.set(value);
    }

    public boolean teleportEnabled() {
        return teleportEnabled.value();
    }

    public void teleportEnabled(boolean value) {
        teleportEnabled.set(value);
    }

    public float teleportDistance() {
        return teleportDistance.value();
    }

    public void teleportDistance(float value) {
        teleportDistance.set(value);
    }

    public boolean considerEntityTeleportationCooldown() {
        return considerEntityTeleportationCooldown.value();
    }

    public void considerEntityTeleportationCooldown(boolean value) {
        considerEntityTeleportationCooldown.set(value);
    }

    public boolean teleportWhenDancing() {
        return teleportWhenDancing.value();
    }

    public void teleportWhenDancing(boolean value) {
        teleportWhenDancing.set(value);
    }

    public boolean avoidTeleportingIntoWater() {
        return avoidTeleportingIntoWater.value();
    }

    public void avoidTeleportingIntoWater(boolean value) {
        avoidTeleportingIntoWater.set(value);
    }

    public boolean avoidTeleportingIntoLava() {
        return avoidTeleportingIntoLava.value();
    }

    public void avoidTeleportingIntoLava(boolean value) {
        avoidTeleportingIntoLava.set(value);
    }

    public boolean avoidTeleportingIntoWalls() {
        return avoidTeleportingIntoWalls.value();
    }

    public void avoidTeleportingIntoWalls(boolean value) {
        avoidTeleportingIntoWalls.set(value);
    }

    public boolean fixLeashBreakingIn_1_19() {
        return fixLeashBreakingIn_1_19.value();
    }

    public void fixLeashBreakingIn_1_19(boolean value) {
        fixLeashBreakingIn_1_19.set(value);
    }

    public int fixLeashBreakingIn_1_19_delay() {
        return fixLeashBreakingIn_1_19_delay.value();
    }

    public void fixLeashBreakingIn_1_19_delay(int value) {
        fixLeashBreakingIn_1_19_delay.set(value);
    }




}

