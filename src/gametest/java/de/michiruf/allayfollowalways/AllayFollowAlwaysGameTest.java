package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.helper.WorldComparator;
import de.michiruf.allayfollowalways.testhelper.*;
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
public class AllayFollowAlwaysGameTest {

    /*? if <1.20.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
     *//*? } elif <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", skyAccess = true, timeoutTicks = 100000)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100000)
     /*?} */
    public void teleport(GameTestHelper context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);

        new TestExecutor(context)
                .immediate(() -> {
                    context.killAllEntities();
                    TestConfigHelper.resetToDefaults();
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.createUniquePlayer();
                    holder.createAllayLinkedToPlayer();
                })
                .then(() -> {
                    var destinationY = holder.player.getY() + 256;
                    Teleport.teleport(holder.player, new Vec3(holder.player.getX(), destinationY, holder.player.getZ()), context.getLevel());
                })
                .then(() -> {
                    var distanceToPlayer = holder.allay.position().distanceTo(holder.player.position());
                    check.assertTrue(AllayFollowAlwaysMod.CONFIG.teleportEnabled(), "Teleport not enabled");
                    check.assertTrue(distanceToPlayer <= 10.0, "Allay is not close to player after teleport. Distance: " + distanceToPlayer + ", Player: " + holder.player.blockPosition() + ", Allay: " + holder.allay.blockPosition());
                })
                .then(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);
                    var destinationY = holder.player.getY() + 256;
                    Teleport.teleport(holder.player, new Vec3(holder.player.getX(), destinationY, holder.player.getZ()), context.getLevel());
                })
                .then(() -> {
                    var distanceToPlayer = holder.allay.position().distanceTo(holder.player.position());
                    check.assertFalse(AllayFollowAlwaysMod.CONFIG.teleportEnabled(), "Teleport enabled");
                    check.assertFalse(distanceToPlayer <= 10.0, "Allay is too close to player and did teleport. Distance: " + distanceToPlayer + ", Player: " + holder.player.blockPosition() + ", Allay: " + holder.allay.blockPosition());
                })
                .then(holder::cleanup)
                .immediate(check::complete)
                .runSync();
    }

    /*? if <1.20.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
     *//*? } elif <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", skyAccess = true, timeoutTicks = 100000)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100000)
     /*?} */
    public void teleportCrossDimension(GameTestHelper context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);
        var nether = new TestWorldHandler(context, Level.NETHER);

        new TestExecutor(context)
                .immediate(() -> {
                    TestConfigHelper.resetToDefaults();
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.createUniquePlayer();
                    holder.createAllayLinkedToPlayer();
                })
                // Force-load nether chunk so non-player entities get tracked in EntityIndex
                .then(() -> nether.forceLoadChunk(0, 0))
                .waitUntil(nether::areAllForcedChunksLoaded)
                // Teleport player
                .then(() -> VersionedPlayerTeleport.teleport(holder.player, new Vec3(0, 64, 0), nether.getWorld()))
                // Relink
                .waitUntil(() -> holder.allayRelinked(nether.getWorld()))
                // Assert allay followed player to the Nether
                .then(() -> {
                    check.assertTrue(
                            WorldComparator.equals(holder.allay, holder.player),
                            "Allay did not follow player to the Nether. Player world registry: " + EntityHelper.getLevel(holder.player).dimension() + ", Allay world registry: " + EntityHelper.getLevel(holder.allay).dimension());
                    var distanceToPlayer = holder.allay.position().distanceTo(holder.player.position());
                    check.assertTrue(
                            distanceToPlayer <= 10.0,
                            "Allay is not close to player in Nether. Distance: " + distanceToPlayer + ", Player: " + holder.player.blockPosition() + ", Allay: " + holder.allay.blockPosition());
                })
                .then(holder::cleanup)
                .immediate(check::complete)
                .runSync();
    }

}
