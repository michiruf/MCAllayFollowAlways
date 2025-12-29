package de.michiruf.allayfollowalways;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.Set;

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
        player.teleport(context.getWorld(), allay.getX(), allay.getY(), allay.getZ(), Set.of(), 0.0f, 0.0f, false);

        // Fake that allay got an item from the player
        allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
    }

    private void clearPlayerAndAllay(TestContext context) {
        context.killEntity(player);
        context.killEntity(allay);
        player = null;
        allay = null;
    }

    @GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
    public void testTeleport(TestContext context) {
        AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
        AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
        createPlayerAndAllay(context);

        var destinationY = player.getY() + 10;
        player.teleport(player.getX(), destinationY, player.getZ(), false);
        context.waitAndRun(20, () -> {
            var distanceToPlayer = allay.getPos().distanceTo(player.getPos());
            context.assertTrue(distanceToPlayer <= 2.0, "Allay is not close to player after teleport. Distance: " + distanceToPlayer + ", Player: " + player.getBlockPos() + ", Allay: " + allay.getBlockPos());

            clearPlayerAndAllay(context);
            context.complete();
        });
    }

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void testTeleportDisabled(TestContext context) {
        AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);
        AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
        createPlayerAndAllay(context);

        var destinationY = player.getY() + 10;
        player.teleport(player.getX(), destinationY, player.getZ(), false);
        context.waitAndRun(20, () -> {
            var distanceToPlayer = allay.getPos().distanceTo(player.getPos());
            context.assertFalse(distanceToPlayer <= 2.0, "Allay is too close to player and did teleport. Distance: " + distanceToPlayer + ", Player: " + player.getBlockPos() + ", Allay: " + allay.getBlockPos());

            clearPlayerAndAllay(context);
            context.complete();
        });
    }

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void testMessages(TestContext context) {
        var player = context.createMockPlayer(GameMode.CREATIVE);
        var commandSource = player.getCommandSource(context.getWorld());
        context.getWorld().getServer().getCommandManager().executeWithPrefix(
                commandSource,
                "/allayfollowalways teleportEnabled"
        );
        context.complete();
    }
}
