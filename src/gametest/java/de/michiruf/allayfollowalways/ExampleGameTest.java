package de.michiruf.allayfollowalways;

//? if <=1.21.4 {
/*import net.minecraft.test.GameTest;
 *///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.test.TestContext;

@SuppressWarnings("unused")
public class ExampleGameTest {

    /*? if <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", tickLimit = 10000)
    *//*?} else { */
    @GameTest(maxTicks = 10000)
    /*?} */
    public void test(TestContext context) {
        context.complete();
    }
}
