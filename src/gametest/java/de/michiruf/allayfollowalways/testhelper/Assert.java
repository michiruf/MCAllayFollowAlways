package de.michiruf.allayfollowalways.testhelper;

import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;

public class Assert {

    private final TestContext context;

    public Assert(TestContext context) {
        this.context = context;
    }

    public void assertTrue(boolean condition, String message) {
        if (!condition)
            throw createException(message);
    }

    public void assertFalse(boolean condition, String message) {
        if (condition)
            throw createException(message);
    }

    public GameTestException createException(String message) {
        //? if <=1.21.4 {
        /*return new GameTestException(message);
        *///? } else {
        return new GameTestException(Text.literal(message), (int) context.getTick());
        //? }
    }

    public void complete() {
        context.complete();
    }
}
