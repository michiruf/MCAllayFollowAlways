package de.michiruf.allayfollowalways.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigWrapper {

    private static final String configFileName = "allayfollowalways.json5";
    private static final File configFile;
    private static final Gson mapper;

    static {
        configFile = FabricLoader.getInstance().getConfigDir().resolve(configFileName).toFile();
        mapper = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    private Config config;

    public static ConfigWrapper load() throws IOException {
        var wrapper = new ConfigWrapper();

        if (!configFile.exists()) {
            AllayFollowAlwaysMod.LOGGER.warn("Config not found, creating new one");
            // Additional save before loading, to have the file initially created
            wrapper.config = new Config();
            wrapper.save();
        }

        wrapper.config = mapper.fromJson(new JsonReader(new FileReader(configFile)), Config.class);
        return wrapper;
    }

    public void save() {
        try {
            var writer = new FileWriter(configFile);
            mapper.toJson(config, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            AllayFollowAlwaysMod.LOGGER.error("Could not save json config file", e);
            throw new RuntimeException(e);
        }
    }

    public double rangeFactor() {
        return config.rangeFactor;
    }

    public void rangeFactor(double rangeFactor) {
        config.rangeFactor = rangeFactor;
        save();
    }

    public float movementSpeedFactor() {
        return config.movementSpeedFactor;
    }

    public void movementSpeedFactor(float movementSpeedFactor) {
        config.movementSpeedFactor = movementSpeedFactor;
        save();
    }

    public boolean teleportEnabled() {
        return config.teleportEnabled;
    }

    public void teleportEnabled(boolean teleportEnabled) {
        config.teleportEnabled = teleportEnabled;
        save();
    }

    public float teleportDistance() {
        return config.teleportDistance;
    }

    public void teleportDistance(float teleportDistance) {
        config.teleportDistance = teleportDistance;
        save();
    }

    public boolean considerEntityTeleportationCooldown() {
        return config.considerEntityTeleportationCooldown;
    }

    public void considerEntityTeleportationCooldown(boolean considerEntityTeleportationCooldown) {
        config.considerEntityTeleportationCooldown = considerEntityTeleportationCooldown;
        save();
    }

    public boolean teleportWhenDancing() {
        return config.teleportWhenDancing;
    }

    public void teleportWhenDancing(boolean teleportWhenDancing) {
        config.teleportWhenDancing = teleportWhenDancing;
        save();
    }

    public boolean avoidTeleportingIntoWater() {
        return config.avoidTeleportingIntoWater;
    }

    public void avoidTeleportingIntoWater(boolean avoidTeleportingIntoWater) {
        config.avoidTeleportingIntoWater = avoidTeleportingIntoWater;
        save();
    }

    public boolean avoidTeleportingIntoLava() {
        return config.avoidTeleportingIntoLava;
    }

    public void avoidTeleportingIntoLava(boolean avoidTeleportingIntoLava) {
        config.avoidTeleportingIntoLava = avoidTeleportingIntoLava;
        save();
    }

    public boolean avoidTeleportingIntoWalls() {
        return config.avoidTeleportingIntoWalls;
    }

    public void avoidTeleportingIntoWalls(boolean avoidTeleportingIntoWalls) {
        config.avoidTeleportingIntoWalls = avoidTeleportingIntoWalls;
        save();
    }

    public LeashMode playerLeashMode() {
        return config.playerLeashMode;
    }

    public void playerLeashMode(LeashMode playerLeashMode) {
        config.playerLeashMode = playerLeashMode;
        save();
    }

    public LeashMode generalLeashMode() {
        return config.generalLeashMode;
    }

    public void generalLeashMode(LeashMode generalLeashMode) {
        config.generalLeashMode = generalLeashMode;
        save();
    }

    public double leashSlowDownDistanceStart() {
        return config.leashSlowDownDistanceStart;
    }

    public void leashSlowDownDistanceStart(double leashSlowDownDistanceStart) {
        config.leashSlowDownDistanceStart = leashSlowDownDistanceStart;
        save();
    }

    public double leashSlowDownDistanceEnd() {
        return config.leashSlowDownDistanceEnd;
    }

    public void leashSlowDownDistanceEnd(double leashSlowDownDistanceEnd) {
        config.leashSlowDownDistanceEnd = leashSlowDownDistanceEnd;
        save();
    }

    public float leashSlowDownDegree() {
        return config.leashSlowDownDegree;
    }

    public void leashSlowDownDegree(float leashSlowDownDegree) {
        config.leashSlowDownDegree = leashSlowDownDegree;
        save();
    }
}
