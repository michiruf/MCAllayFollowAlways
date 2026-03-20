package de.michiruf.allayfollowalways;

//? if >= 1.19.4 {
import de.michiruf.allayfollowalways.helper.WorldComparator;
import de.michiruf.allayfollowalways.testhelper.Assert;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
import de.michiruf.allayfollowalways.testhelper.TestObjectHolder;
import de.michiruf.allayfollowalways.testhelper.VersionedPlayerTeleport;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.passive.AllayEntity;
import de.michiruf.allayfollowalways.versioned.EntityHelper;
import net.minecraft.world.World;
//? if <=1.21.4 {
/*import net.minecraft.test.GameTest;
 *///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.test.TestContext;
import net.minecraft.util.math.Vec3d;
//? }

@SuppressWarnings("unused")
public class AllayFollowAlwaysGameTest {

    // Since fabric has no fake player in versions until 1.19.4, we cannot test the teleport behavior
    // We need the fake player, because mocked players are not registered properly
    //? if >= 1.19.4 {

    /*? if <1.20.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
     *//*? } elif <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
     *//*?} else { */
    @GameTest(skyAccess = true)
     /*?} */
    public void teleport(TestContext context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);

        new TestExecutor(context)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.createUniquePlayer();
                    holder.createAllayLinkedToPlayer();
                })
                .then(() -> {
                    var destinationY = holder.player.getY() + 256;
                    VersionedFabricTeleport.teleport(holder.player, new Vec3d(holder.player.getX(), destinationY, holder.player.getZ()), context.getWorld());
                })
                .then(() -> {
                    var distanceToPlayer = EntityHelper.getPos(holder.allay).distanceTo(EntityHelper.getPos(holder.player));
                    check.assertTrue(AllayFollowAlwaysMod.CONFIG.teleportEnabled(), "Teleport not enabled");
                    check.assertTrue(distanceToPlayer <= 10.0, "Allay is not close to player after teleport. Distance: " + distanceToPlayer + ", Player: " + holder.player.getBlockPos() + ", Allay: " + holder.allay.getBlockPos());
                })
                .then(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);
                    var destinationY = holder.player.getY() + 256;
                    VersionedFabricTeleport.teleport(holder.player, new Vec3d(holder.player.getX(), destinationY, holder.player.getZ()), context.getWorld());
                })
                .then(() -> {
                    var distanceToPlayer = EntityHelper.getPos(holder.allay).distanceTo(EntityHelper.getPos(holder.player));
                    check.assertFalse(AllayFollowAlwaysMod.CONFIG.teleportEnabled(), "Teleport enabled");
                    check.assertFalse(distanceToPlayer <= 10.0, "Allay is too close to player and did teleport. Distance: " + distanceToPlayer + ", Player: " + holder.player.getBlockPos() + ", Allay: " + holder.allay.getBlockPos());
                })
                .then(holder::cleanup)
                .immediate(check::complete)
                .runSync();
    }

    /*? if <1.20.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
     *//*? } elif <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
     *//*?} else { */
    @GameTest(skyAccess = true)
     /*?} */
    public void teleportCrossDimension(TestContext context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);
        var netherWorld = context.getWorld().getServer().getWorld(World.NETHER);

        new TestExecutor(context)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoLava(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown(false);
                    holder.createUniquePlayer();
                    holder.createAllayLinkedToPlayer();
                })
                .then(() -> VersionedPlayerTeleport.teleport(holder.player, new Vec3d(0, 64, 0), netherWorld))
                .then(() -> holder.relinkAllayForWorld(netherWorld)) // important!
                .then(() -> {
                    // Assert allay followed player to the Nether
                    check.assertTrue(
                            WorldComparator.equals(holder.allay, holder.player),
                            "Allay did not follow player to the Nether. Player world registry: " + EntityHelper.getWorld(holder.player).getRegistryKey() + ", Allay world registry: " + EntityHelper.getWorld(holder.allay).getRegistryKey());
                    var distanceToPlayer = EntityHelper.getPos(holder.allay).distanceTo(EntityHelper.getPos(holder.player));
                    check.assertTrue(
                            distanceToPlayer <= 10.0,
                            "Allay is not close to player in Nether. Distance: " + distanceToPlayer + ", Player: " + holder.player.getBlockPos() + ", Allay: " + holder.allay.getBlockPos());
                })
                .then(holder::cleanup)
                .immediate(check::complete)
                .runSync();
    }

    //? }
}
