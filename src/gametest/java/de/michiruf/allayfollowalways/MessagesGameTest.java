package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.testhelper.TestExecutor;

//? if <=1.21.4 {
/*import net.minecraft.gametest.framework.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
//? if >=1.20.5 {
import net.minecraft.world.level.GameType;
//? }

@SuppressWarnings("unused")
public class MessagesGameTest {

    /*? if <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
    *//*?} else { */
    @GameTest(maxTicks = 100000)
    /*?} */
    public void messages(GameTestHelper context) {
        var holder = new Object() {
            Entity player;
        };

        new TestExecutor(context)
                .immediate(context::killAllEntities)
                .immediate(() -> {
                    //? if <1.19.3 {
                    /*holder.player = context.makeMockPlayer();
                    *///? } elif <1.20.5 {
                    /*holder.player = context.makeMockPlayer();
                    *///? } else {
                    holder.player = context.makeMockPlayer(GameType.CREATIVE);
                    //? }
                })
                .then(() -> {
                    //? if <1.21.10 {
                    /*context.getLevel().getServer().getCommands().performPrefixedCommand(
                    *///? } else {
                    context.getLevel().getServer().getCommands().performPrefixedCommand(
                    //? }
                            //? if <1.21.2 {
                            /*holder.player.createCommandSourceStack(),
                            *///? } else {
                            holder.player.createCommandSourceStackForNameResolution(context.getLevel()),
                            //? }
                            "/allayfollowalways teleportEnabled"
                    );
                })
                .immediate(() -> holder.player.remove(Entity.RemovalReason.DISCARDED))
                .immediate(context::succeed)
                .runSync();
    }
}
