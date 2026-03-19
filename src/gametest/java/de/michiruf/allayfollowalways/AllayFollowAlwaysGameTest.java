package de.michiruf.allayfollowalways;

//? if >= 1.19.4 {
import de.michiruf.allayfollowalways.helper.WorldComparator;
import de.michiruf.allayfollowalways.testhelper.Assert;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
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
        final var holder = new Object() {
            FakePlayer player;
            AllayEntity allay;
        };

        new TestExecutor(context)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.player = check.createUniquePlayer();
                    holder.allay = check.createAllayLinkedTo(holder.player);
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
                .immediate(check::complete)
                .run();
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
        final var holder = new Object() {
            FakePlayer player;
            AllayEntity allay;
        };

        new TestExecutor(context)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoLava(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    AllayFollowAlwaysMod.CONFIG.considerEntityTeleportationCooldown(false);
                    holder.player = check.createUniquePlayer();
                    holder.allay = check.createAllayLinkedTo(holder.player);
                })
                .then(() -> {
                    // Teleport player to the Nether
                    var netherWorld = context.getWorld().getServer().getWorld(World.NETHER);
                    VersionedPlayerTeleport.teleport(holder.player, new Vec3d(0, 64, 0), netherWorld);
                })
                .then(() -> {
                    // Assert allay followed player to the Nether
                    check.assertTrue(
                            WorldComparator.equals(holder.allay, holder.player),
                            "Allay did not follow player to the Nether. Player world: " + EntityHelper.getWorld(holder.player) + ", Allay world: " + EntityHelper.getWorld(holder.allay));
                    var distanceToPlayer = EntityHelper.getPos(holder.allay).distanceTo(EntityHelper.getPos(holder.player));
                    check.assertTrue(
                            distanceToPlayer <= 10.0,
                            "Allay is not close to player in Nether. Distance: " + distanceToPlayer + ", Player: " + holder.player.getBlockPos() + ", Allay: " + holder.allay.getBlockPos());
                })
                .immediate(check::complete)
                .run();
    }
    //? }
}
