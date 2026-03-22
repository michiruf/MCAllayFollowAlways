package de.michiruf.allayfollowalways.helper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * @author Michael Ruf
 * @since 2022-12-02
 */
public class WorldComparator {

    public static boolean equals(Entity one, Entity two) {
        return equals(EntityHelper.getLevel(one), EntityHelper.getLevel(two));
    }

    public static boolean equals(Entity one, Level two) {
        return equals(EntityHelper.getLevel(one), two);
    }

    public static boolean equals(Level one, Entity two) {
        return equals(one, EntityHelper.getLevel(two));
    }

    public static boolean equals(Level one, Level two) {
        return one == two && one.dimensionType().equals(two.dimensionType());
    }
}
