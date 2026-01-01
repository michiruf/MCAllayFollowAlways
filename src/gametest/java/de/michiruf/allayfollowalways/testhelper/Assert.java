package de.michiruf.allayfollowalways.testhelper;

import net.minecraft.test.TestContext;

public class Assert {

    private static TestContext context;

    public static void init(TestContext context) {
        Assert.context = context;
    }

    public static void assertTrue(boolean condition, String message) {
        context.assertTrue(condition, text(message));
    }

    public static void assertFalse(boolean condition, String message) {
        //? if <1.21.5 {
        /*context.assertTrue(! condition, text(message));
        *///? } else {
        context.assertFalse(condition, text(message));
        //? }
    }

    //? if <=1.21.5 {
    /*private static String text(String message) {
        return message;
    }
    *///? } else {
    private static net.minecraft.text.Text text(String message) {
        return net.minecraft.text.Text.literal(message);
    }
    //? }

    public static void complete() {
        context.complete();
        context = null;
    }
}
