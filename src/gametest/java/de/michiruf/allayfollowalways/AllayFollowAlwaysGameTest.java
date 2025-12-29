package de.michiruf.allayfollowalways;

import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

public class AllayFollowAlwaysGameTest {

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void helloWorld(TestContext context) {
        // This test just verifies the GameTest framework is working
        // If we get here, the server started and the test ran successfully
        context.complete();
    }
}
