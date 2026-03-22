package de.michiruf.allayfollowalways.testhelper;

import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;

public class Assert {

    private final GameTestHelper context;

    public Assert(GameTestHelper context) {
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

    public GameTestAssertException createException(String message) {
        //? if <=1.21.4 {
        /*return new GameTestAssertException(message);
        *///? } else {
        return new GameTestAssertException(Component.literal(message), (int) context.getTick());
        //? }
    }

    public void complete() {
        context.succeed();
    }
}
