package de.michiruf.allayfollowalways;

//? if <=1.21.4 {
/*import net.minecraft.gametest.framework.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.gametest.framework.GameTestHelper;

@SuppressWarnings("unused")
public class ExampleGameTest {

    /*? if <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
    *//*?} else { */
    @GameTest(maxTicks = 100000)
    /*?} */
    public void test(GameTestHelper context) {
        context.succeed();
    }
}
