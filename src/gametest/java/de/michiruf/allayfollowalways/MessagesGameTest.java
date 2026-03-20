package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.testhelper.TestExecutor;

//? if <=1.21.4 {
/*import net.minecraft.test.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
 //? }
import net.minecraft.entity.Entity;
//? if <1.19.3 {
/*import net.minecraft.server.network.ServerPlayerEntity;
*///? } else {
import net.minecraft.server.network.ServerPlayerEntity;
//? }
import net.minecraft.test.TestContext;
//? if >=1.20.5 {
import net.minecraft.world.GameMode;
//? }

@SuppressWarnings("unused")
public class MessagesGameTest {

    /*? if <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", tickLimit = 1000)
    *//*?} else { */
    @GameTest(maxTicks = 1000)
    /*?} */
    public void messages(TestContext context) {
        var holder = new Object() {
            Entity player;
        };

        new TestExecutor(context)
                .immediate(context::killAllEntities)
                .immediate(() -> {
                    //? if <1.19.3 {
                    /*holder.player = context.createMockPlayer();
                    *///? } elif <1.20.5 {
                    /*holder.player = context.createMockCreativePlayer();
                    *///? } else {
                    holder.player = context.createMockPlayer(GameMode.CREATIVE);
                    //? }
                })
                .then(() -> {
                    //? if <1.21.10 {
                    /*context.getWorld().getServer().getCommandManager().executeWithPrefix(
                    *///? } else {
                    context.getWorld().getServer().getCommandManager().parseAndExecute(
                    //? }
                            //? if <1.21.2 {
                            /*holder.player.getCommandSource(),
                            *///? } else {
                            holder.player.getCommandSource(context.getWorld()),
                            //? }
                            "/allayfollowalways teleportEnabled"
                    );
                })
                .immediate(() -> holder.player.remove(Entity.RemovalReason.DISCARDED))
                .immediate(context::complete)
                .runSync();
    }
}
