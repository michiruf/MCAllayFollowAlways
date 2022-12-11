package de.michiruf.allayfollowalways.helper;

import net.minecraft.util.math.Vec3d;

/**
 * @author Michael Ruf
 * @since 2022-12-11
 */
public class MyMathHelper {

    public static double angleBetween(Vec3d a, Vec3d b, boolean vectorsAreNormalized) {
        if (!vectorsAreNormalized) {
            a = a.normalize();
            b = b.normalize();
        }
        // Got from: https://stackoverflow.com/a/28596355
        return 2.0 * Math.atan((a.subtract(b)).length() / (a.add(b)).length());
    }

    public static double angleBetweenDeg(Vec3d a, Vec3d b, boolean vectorsAreNormalized) {
        return angleBetween(a, b, vectorsAreNormalized) * 180 / Math.PI;
    }
}
