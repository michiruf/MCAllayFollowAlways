package de.michiruf.allayfollowalways.helper;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * @author Michael Ruf
 * @since 2025-12-31
 */
public class EntityHelper {

    public static Level getLevel(Entity entity) {
        //? if <1.20 {
        /*return entity.getLevel();
        *///? } else {
        return entity.level();
        //? }
    }

    public static ServerLevel getServerLevel(Entity entity) {
        return (ServerLevel) getLevel(entity);
    }
}
