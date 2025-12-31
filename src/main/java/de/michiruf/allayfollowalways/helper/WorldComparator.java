package de.michiruf.allayfollowalways.helper;

import de.michiruf.allayfollowalways.versioned.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * @author Michael Ruf
 * @since 2022-12-02
 */
public class WorldComparator {

    public static boolean equals(Entity one, Entity two) {
        return equals(EntityHelper.getWorld(one), EntityHelper.getWorld(two));
    }

    public static boolean equals(World one, World two) {
        return one == two && one.getDimension().equals(two.getDimension());
    }
}
