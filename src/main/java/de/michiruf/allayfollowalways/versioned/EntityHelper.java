package de.michiruf.allayfollowalways.versioned;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @author Michael Ruf
 * @since 2025-12-31
 */
public class EntityHelper {

    public static Vec3 getPos(Entity entity) {
        return entity.position();
    }

    public static Level getWorld(Entity entity) {
        //? if <1.20 {
        /*return entity.getLevel();
        *///? } else {
        return entity.level();
        //? }
    }

    public static ServerLevel getServerWorld(Entity entity) {
        return (ServerLevel) getWorld(entity);
    }
}
