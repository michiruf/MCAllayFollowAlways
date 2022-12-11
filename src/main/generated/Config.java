package de.michiruf.allayfollowalways.config;

import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Config extends ConfigWrapper<de.michiruf.allayfollowalways.config.ConfigModel> {

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
    private final Option<de.michiruf.allayfollowalways.config.LeashMode> playerLeashMode = this.optionForKey(new Option.Key("playerLeashMode"));
    private final Option<de.michiruf.allayfollowalways.config.LeashMode> generalLeashMode = this.optionForKey(new Option.Key("generalLeashMode"));
    private final Option<java.lang.Double> leashSlowDownDistanceStart = this.optionForKey(new Option.Key("leashSlowDownDistanceStart"));
    private final Option<java.lang.Double> leashSlowDownDistanceEnd = this.optionForKey(new Option.Key("leashSlowDownDistanceEnd"));
    private final Option<java.lang.Float> leashSlowDownDegree = this.optionForKey(new Option.Key("leashSlowDownDegree"));

    private Config() {
        super(de.michiruf.allayfollowalways.config.ConfigModel.class);
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

    public de.michiruf.allayfollowalways.config.LeashMode playerLeashMode() {
        return playerLeashMode.value();
    }

    public void playerLeashMode(de.michiruf.allayfollowalways.config.LeashMode value) {
        playerLeashMode.set(value);
    }

    public de.michiruf.allayfollowalways.config.LeashMode generalLeashMode() {
        return generalLeashMode.value();
    }

    public void generalLeashMode(de.michiruf.allayfollowalways.config.LeashMode value) {
        generalLeashMode.set(value);
    }

    public double leashSlowDownDistanceStart() {
        return leashSlowDownDistanceStart.value();
    }

    public void leashSlowDownDistanceStart(double value) {
        leashSlowDownDistanceStart.set(value);
    }

    public double leashSlowDownDistanceEnd() {
        return leashSlowDownDistanceEnd.value();
    }

    public void leashSlowDownDistanceEnd(double value) {
        leashSlowDownDistanceEnd.set(value);
    }

    public float leashSlowDownDegree() {
        return leashSlowDownDegree.value();
    }

    public void leashSlowDownDegree(float value) {
        leashSlowDownDegree.set(value);
    }




}

