package de.michiruf.allayfollowalways;

import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

@SuppressWarnings("unused")
public class ExampleGameTest {

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void test(TestContext context) {
        context.complete();
    }
}
