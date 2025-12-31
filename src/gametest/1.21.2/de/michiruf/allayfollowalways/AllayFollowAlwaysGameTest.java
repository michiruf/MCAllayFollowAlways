package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

@SuppressWarnings("unused")
public class AllayFollowAlwaysGameTest {

    private FakePlayer player;
    private AllayEntity allay;

    @GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
    public void teleport(TestContext context) {
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
                    var distanceToPlayer = allay.getPos().distanceTo(player.getPos());
                    context.assertTrue(AllayFollowAlwaysMod.CONFIG.teleportEnabled(), "Teleport not enabled");
                    context.assertTrue(distanceToPlayer <= 10.0, "Allay is not close to player after teleport. Distance: " + distanceToPlayer + ", Player: " + player.getBlockPos() + ", Allay: " + allay.getBlockPos());
                })
                .then(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);
                    var destinationY = player.getY() + 256;
                    VersionedFabricTeleport.teleport(player, new Vec3d(player.getX(), destinationY, player.getZ()), context.getWorld());
                })
                .then(() -> {
                    var distanceToPlayer = allay.getPos().distanceTo(player.getPos());
                    context.assertFalse(AllayFollowAlwaysMod.CONFIG.teleportEnabled(), "Teleport enabled");
                    context.assertFalse(distanceToPlayer <= 10.0, "Allay is too close to player and did teleport. Distance: " + distanceToPlayer + ", Player: " + player.getBlockPos() + ", Allay: " + allay.getBlockPos());
                })
                .immediate(context::complete)
                .run();
    }

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void messages(TestContext context) {
        var player = context.createMockPlayer(GameMode.CREATIVE);
        context.getWorld().getServer().getCommandManager().executeWithPrefix(
                player.getCommandSource(context.getWorld()),
                "/allayfollowalways teleportEnabled"
        );
        context.complete();
    }
}
