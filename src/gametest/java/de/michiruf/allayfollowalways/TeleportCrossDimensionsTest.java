package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.testhelper.Assert;
import de.michiruf.allayfollowalways.testhelper.TestConfigHelper;
import de.michiruf.allayfollowalways.testhelper.TestWorldHandler;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
import de.michiruf.allayfollowalways.testhelper.TestObjectHolder;
import de.michiruf.allayfollowalways.testhelper.VersionedPlayerTeleport;
import de.michiruf.allayfollowalways.helper.Teleport;
//? if <=1.21.4 {
/*import net.minecraft.gametest.framework.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import de.michiruf.allayfollowalways.helper.EntityHelper;

@SuppressWarnings("unused")
public class TeleportCrossDimensionsTest {

    /*? if <1.20.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
     *//*? } elif <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", skyAccess = true, timeoutTicks = 100000)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100000)
     /*?} */
    public void manualTeleportCrossDimension(GameTestHelper context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);
        var nether = new TestWorldHandler(context, Level.NETHER);

        new TestExecutor(context)
                .immediate(() -> {
                    context.killAllEntities();
                    TestConfigHelper.resetToDefaults();
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);
                    holder.createUniquePlayer();
                    holder.createAllay(); // not linked in this test
                })
                // Player
                .then(() -> VersionedPlayerTeleport.teleport(holder.player, new Vec3(0, 0, 0), nether.getWorld()))
                .then(() -> check.assertTrue(
                        EntityHelper.getLevel(holder.player).dimension() == Level.NETHER,
                        "Player is not in the Nether. Player world: " + EntityHelper.getLevel(holder.player).dimension()))
                // Force-load nether chunk so non-player entities get tracked in EntityIndex
                .then(() -> nether.forceLoadChunk(0, 0))
                .waitUntil(nether::areAllForcedChunksLoaded)
                // Allay
                .then(() -> Teleport.teleport(holder.allay, new Vec3(0, 0, 0), nether.getWorld()))
                .waitUntil(() -> holder.allayRelinked(nether.getWorld()))
                .then(() -> check.assertTrue(
                        EntityHelper.getLevel(holder.allay).dimension() == Level.NETHER,
                        "Allay is not in the Nether. Allay world: " + EntityHelper.getLevel(holder.allay).dimension()))
                // Finalize
                .immediate(nether::cleanup)
                .immediate(holder::cleanup)
                .immediate(check::complete)
                .runSync();
    }

}
