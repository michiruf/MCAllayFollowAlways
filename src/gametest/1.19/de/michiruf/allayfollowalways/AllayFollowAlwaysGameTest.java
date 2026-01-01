package de.michiruf.allayfollowalways;

import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

@SuppressWarnings("unused")
public class AllayFollowAlwaysGameTest {

    // Since fabric has no fake player in versions until 1.19.4, we cannot test the teleport behavior
    // We need the fake player, because mocked players are not registered properly
    // However, with this test being present, the mod will be installed and at least checked somehow

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void noTest(TestContext context) {
        context.complete();
    }
}
