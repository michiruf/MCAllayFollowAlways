package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

@SuppressWarnings("unused")
public class AllayFollowAlwaysGameTest {

    private FakePlayer player;
    private AllayEntity allay;

    private void createPlayerAndAllay(TestContext context) {
        if (player != null && allay != null)
            return;

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

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void testTeleport(TestContext context) {
        AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
        AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
        AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
        AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
        createPlayerAndAllay(context);

        var destinationY = player.getY() + 256;
        VersionedFabricTeleport.teleport(player, new Vec3d(player.getX(), destinationY, player.getZ()), context.getWorld());
        context.waitAndRun(20, () -> {
            var distanceToPlayer = allay.getPos().distanceTo(player.getPos());
            context.assertTrue(distanceToPlayer <= 10.0, "Allay is not close to player after teleport. Distance: " + distanceToPlayer + ", Player: " + player.getBlockPos() + ", Allay: " + allay.getBlockPos());
            context.complete();
        });
    }

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void testTeleportDisabled(TestContext context) {
        AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);
        AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
        AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
        AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
        createPlayerAndAllay(context);

        var destinationY = player.getY() + 256;
        VersionedFabricTeleport.teleport(player, new Vec3d(player.getX(), destinationY, player.getZ()), context.getWorld());
        context.waitAndRun(20, () -> {
            var distanceToPlayer = allay.getPos().distanceTo(player.getPos());
            context.assertTrue(distanceToPlayer > 10.0, "Allay is too close to player and did teleport. Distance: " + distanceToPlayer + ", Player: " + player.getBlockPos() + ", Allay: " + allay.getBlockPos());
            context.complete();
        });
    }

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void testMessages(TestContext context) {
        var player = context.createMockCreativePlayer();
        context.getWorld().getServer().getCommandManager().executeWithPrefix(
                player.getCommandSource(),
                "/allayfollowalways teleportEnabled"
        );
        context.complete();
    }
}
