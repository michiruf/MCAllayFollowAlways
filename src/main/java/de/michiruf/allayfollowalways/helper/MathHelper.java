package de.michiruf.allayfollowalways.helper;

import net.minecraft.world.phys.Vec3;

/**
 * @author Michael Ruf
 * @since 2022-12-11
 */
public class MathHelper {

    // NOTE Can this get improved to not need to normalize? Should cost some performance like this
    public static double angleBetween(Vec3 a, Vec3 b, boolean vectorsAreNormalized) {
        if (!vectorsAreNormalized) {
            a = a.normalize();
            b = b.normalize();
        }
        // Got from: https://stackoverflow.com/a/28596355
        return 2.0 * Math.atan((a.subtract(b)).length() / (a.add(b)).length());
    }

    public static double angleBetweenDeg(Vec3 a, Vec3 b, boolean vectorsAreNormalized) {
        return angleBetween(a, b, vectorsAreNormalized) * 180 / Math.PI;
    }
}
