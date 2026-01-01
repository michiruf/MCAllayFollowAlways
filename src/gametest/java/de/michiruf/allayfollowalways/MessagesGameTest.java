package de.michiruf.allayfollowalways;

//? if <=1.21.5 {
/*import net.minecraft.test.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
 //? }
import net.minecraft.test.TestContext;
//? if >=1.20.5 {
import net.minecraft.world.GameMode;
//? }

@SuppressWarnings("unused")
public class MessagesGameTest {

    /*? if <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
    *//*?} else { */
    @GameTest
    /*?} */
    public void messages(TestContext context) {
        //? if <1.19.3 {
        /*var player = context.createMockPlayer();
        *///? } elif <1.20.5 {
        /*var player = context.createMockCreativePlayer();
        *///? } else {
        var player = context.createMockPlayer(GameMode.CREATIVE);
        //? }

        //? if <1.21.10 {
        /*context.getWorld().getServer().getCommandManager().executeWithPrefix(
        *///? } else {
        context.getWorld().getServer().getCommandManager().parseAndExecute(
        //? }
                //? if <1.21.2 {
                /*player.getCommandSource(),
                *///? } else {
                player.getCommandSource(context.getWorld()),
                //? }
                "/allayfollowalways teleportEnabled"
        );

        context.complete();
    }
}
