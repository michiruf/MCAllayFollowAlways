package de.michiruf.allayfollowalways.versioned;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Michael Ruf
 * @since 2025-12-31
 */
public class EntityHelper {

    public static Vec3d getPos(Entity entity) {
        return entity.getPos();
    }

    public static World getWorld(Entity entity) {
        return entity.getWorld();
    }

    public static ServerWorld getServerWorld(Entity entity) {
        return (ServerWorld) entity.getWorld();
    }
}
