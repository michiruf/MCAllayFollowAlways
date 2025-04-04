package de.michiruf.allayfollowalways.helper;

import net.minecraft.entity.Entity;

/**
 * @author Michael Ruf
 * @since 2022-12-01
 */
public class DebugEntity {

    public static String idString(Object entity) {
        return idString((Entity) entity);
    }

    public static String idString(Entity entity) {
        return entity.getType().getName().getString() + " '" + entity.getName().getString() + "' [" + entity.getWorld().getDimensionEntry().value().toString() + "]";
    }
}
