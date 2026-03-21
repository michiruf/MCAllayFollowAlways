package de.michiruf.allayfollowalways.helper;

import de.michiruf.allayfollowalways.versioned.EntityHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * @author Michael Ruf
 * @since 2022-12-02
 */
public class WorldComparator {

    public static boolean equals(Entity one, Entity two) {
        return equals(EntityHelper.getWorld(one), EntityHelper.getWorld(two));
    }

    public static boolean equals(Entity one, Level two) {
        return equals(EntityHelper.getWorld(one), two);
    }

    public static boolean equals(Level one, Entity two) {
        return equals(one, EntityHelper.getWorld(two));
    }

    public static boolean equals(Level one, Level two) {
        return one == two && one.dimensionType().equals(two.dimensionType());
    }
}
