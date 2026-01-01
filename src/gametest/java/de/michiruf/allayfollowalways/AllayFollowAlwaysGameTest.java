package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.testhelper.Assert;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
import de.michiruf.allayfollowalways.testhelper.TestSetup;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.passive.AllayEntity;
import de.michiruf.allayfollowalways.versioned.EntityHelper;
//? if <=1.21.5 {
/*import net.minecraft.test.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//?}
import net.minecraft.test.TestContext;
import net.minecraft.util.math.Vec3d;
//? if >=1.20.5 {
import net.minecraft.world.GameMode;
//?}

@SuppressWarnings("unused")
public class AllayFollowAlwaysGameTest {

    private FakePlayer player;
    private AllayEntity allay;

    /*? if <1.20.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
    *//*? } elif <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
    *//*?} else { */
    @GameTest(skyAccess = true)
    /*?} */
    public void teleport(TestContext context) {
        Assert.init(context);

        new TestExecutor(context)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    var setup = TestSetup.createPlayerAndAllay(context);
                    player = setup.getLeft();
                    allay = setup.getRight();
                })
                .then(() -> {
                    var destinationY = player.getY() + 256;
                    VersionedFabricTeleport.teleport(player, new Vec3d(player.getX(), destinationY, player.getZ()), context.getWorld());
                })
                .then(() -> {
                    var distanceToPlayer = EntityHelper.getPos(allay).distanceTo(EntityHelper.getPos(player));
                    Assert.assertTrue(AllayFollowAlwaysMod.CONFIG.teleportEnabled(), "Teleport not enabled");
                    Assert.assertTrue(distanceToPlayer <= 10.0, "Allay is not close to player after teleport. Distance: " + distanceToPlayer + ", Player: " + player.getBlockPos() + ", Allay: " + allay.getBlockPos());
                })
                .then(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);
                    var destinationY = player.getY() + 256;
                    VersionedFabricTeleport.teleport(player, new Vec3d(player.getX(), destinationY, player.getZ()), context.getWorld());
                })
                .then(() -> {
                    var distanceToPlayer = EntityHelper.getPos(allay).distanceTo(EntityHelper.getPos(player));
                    Assert.assertFalse(AllayFollowAlwaysMod.CONFIG.teleportEnabled(), "Teleport enabled");
                    Assert.assertFalse(distanceToPlayer <= 10.0, "Allay is too close to player and did teleport. Distance: " + distanceToPlayer + ", Player: " + player.getBlockPos() + ", Allay: " + allay.getBlockPos());
                })
                .immediate(Assert::complete)
                .run();
    }

    /*? if <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
    *//*?} else { */
    @GameTest
    /*?} */
    public void messages(TestContext context) {
        //? if <1.20.5 {
        /*var player = context.createMockCreativePlayer();
        *///?} else {
        var player = context.createMockPlayer(GameMode.CREATIVE);
        //?}
        //? if <1.21.10 {
        /*context.getWorld().getServer().getCommandManager().executeWithPrefix(
        *///?} else {
        context.getWorld().getServer().getCommandManager().parseAndExecute(
        //?}
                //? if <1.21.2 {
                /*player.getCommandSource(),
                *///?} else {
                player.getCommandSource(context.getWorld()),
                //?}
                "/allayfollowalways teleportEnabled"
        );
        context.complete();
    }
}
