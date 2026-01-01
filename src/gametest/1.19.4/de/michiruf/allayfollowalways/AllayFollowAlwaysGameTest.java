package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.testhelper.Assert;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import de.michiruf.allayfollowalways.versioned.EntityHelper;
//? if <=1.21.5 {
/*import net.minecraft.test.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

@SuppressWarnings("unused")
public class AllayFollowAlwaysGameTest {

    private FakePlayer player;
    private AllayEntity allay;

    private void createPlayerAndAllay(TestContext context) {
        player = FakePlayer.get(context.getWorld());
        player.changeGameMode(GameMode.SURVIVAL);

        // Register player in the entity registry, necessary for lookups with uuid
        context.getWorld().onPlayerConnected(player);

        allay = context.spawnEntity(EntityType.ALLAY, new BlockPos(0, 1, 0));

        // Teleport player to allay, because the player is not spawned in the test space
        VersionedFabricTeleport.teleport(player, allay, context.getWorld());

        // Fake that allay got an item from the player
        allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
    }

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
                    createPlayerAndAllay(context);
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
}
