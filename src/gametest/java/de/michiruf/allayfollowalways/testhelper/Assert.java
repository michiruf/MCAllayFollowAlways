package de.michiruf.allayfollowalways.testhelper;

import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;

public class Assert {

    private static TestContext context;

    public static void init(TestContext context) {
        Assert.context = context;
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition)
            throw new GameTestException(Text.literal(message), (int) context.getTick());
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition)
            throw new GameTestException(Text.literal(message), (int) context.getTick());
    }

    public static void complete() {
        context.complete();
        context = null;
    }
}
