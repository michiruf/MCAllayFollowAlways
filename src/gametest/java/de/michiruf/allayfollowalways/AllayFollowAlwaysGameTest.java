package de.michiruf.allayfollowalways;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.Set;

@SuppressWarnings("unused")
public class AllayFollowAlwaysGameTest {

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void testTeleport(TestContext context) {
        assert AllayFollowAlwaysMod.CONFIG.teleportEnabled() : "Teleportation must be enabled for this test";

        var player = context.createMockPlayer(GameMode.SURVIVAL);
        var allay = context.spawnEntity(
                net.minecraft.entity.EntityType.ALLAY,
                new BlockPos(0, 1, 0)
        );
        allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());

        Vec3d initialAllayPos = allay.getPos();
        player.teleport(context.getWorld(), 100.0, 1.0, 0.0, Set.of(), 0.0f, 0.0f, false);
        context.waitAndRun(1000, () -> {
            double distanceToPlayer = allay.getPos().distanceTo(player.getPos());
            assert distanceToPlayer <= 10.0 : "Allay is not close to player after teleport. Distance: " + distanceToPlayer;

            context.complete();
        });
    }

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void testDisabled(TestContext context) {
        try {
            AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);

            var player = context.createMockPlayer(GameMode.SURVIVAL);
            AllayEntity allay = context.spawnEntity(
                    net.minecraft.entity.EntityType.ALLAY,
                    new BlockPos(0, 1, 0)
            );
            allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());

            Vec3d initialAllayPos = allay.getPos();
            player.teleport(context.getWorld(), 100.0, 1.0, 0.0, Set.of(), 0.0f, 0.0f, false);
            context.waitAndRun(10, () -> {
                double distanceMoved = allay.getPos().distanceTo(initialAllayPos);
                assert distanceMoved > 10.0 : "Allay teleported when disabled! Distance moved: " + distanceMoved;

                context.complete();
            });
        } finally {
            AllayFollowAlwaysMod.CONFIG.teleportEnabled(true);
        }
    }

    @GameTest(templateName = "fabric-gametest-api-v1:empty")
    public void testMessages(TestContext context) {
        var player = context.createMockPlayer(GameMode.CREATIVE);
        var commandSource = player.getCommandSource((ServerWorld) player.getEntityWorld());
        context.getWorld().getServer().getCommandManager().executeWithPrefix(
                commandSource,
                "/allayfollowalways teleportEnabled"
        );
        context.complete();
    }
}
