package de.michiruf.allayfollowalways.helper;

import net.minecraft.util.math.Vec3d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Michael Ruf
 * @since 2022-12-11
 */
public class MyMathHelperTest {

    private static final Vec3d left = new Vec3d(1, 0, 0);
    private static final Vec3d right = new Vec3d(-1, 0, 0);
    private static final Vec3d up = new Vec3d(0, 1, 0);
    private static final Vec3d down = new Vec3d(0, -1, 0);
    private static final Vec3d forward = new Vec3d(0, 0, 1);
    private static final Vec3d backward = new Vec3d(0, 0, -1);
    private static final Vec3d zero = Vec3d.ZERO;

    @Test
    public void angleBetweenTest() {
        Assertions.assertEquals(0, MyMathHelper.angleBetween(up, up, true));
        Assertions.assertEquals(0, MyMathHelper.angleBetweenDeg(up, up, true));

        Assertions.assertEquals(Math.PI / 2.0, MyMathHelper.angleBetween(up, left, true));
        Assertions.assertEquals(90, MyMathHelper.angleBetweenDeg(up, left, true));

        Assertions.assertEquals(Math.PI, MyMathHelper.angleBetween(up, down, true));
        Assertions.assertEquals(180, MyMathHelper.angleBetweenDeg(up, down, true));

        // Real world example
        // Wolframalpha says no
        // https://www.wolframalpha.com/input?i=angle+between+%280.05121958823555278%2C+4.499998822808226E-4%2C+-0.001248387590448024%29%2C+%28-0.17256256739075937%2C+0.6214511069183004%2C+-2.691143317705169%29
        Assertions.assertEquals(92.10146543246499, MyMathHelper.angleBetweenDeg(
                new Vec3d(0.05121958823555278, 4.499998822808226E-4, -0.001248387590448024),
                new Vec3d(-0.17256256739075937, 0.6214511069183004, -2.691143317705169),
                false));


        Assertions.assertEquals(101.0958032831364, MyMathHelper.angleBetweenDeg(new Vec3d(5, 1, -1), forward, false));
        Assertions.assertEquals(57.02103795287812, MyMathHelper.angleBetweenDeg(new Vec3d(-5, -1, -1), new Vec3d(-1, 0, 1), false));
    }
}
