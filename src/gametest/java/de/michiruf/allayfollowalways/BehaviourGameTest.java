package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.testhelper.Assert;
import de.michiruf.allayfollowalways.testhelper.TestConfigHelper;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
import de.michiruf.allayfollowalways.testhelper.TestObjectHolder;
import de.michiruf.allayfollowalways.helper.EntityHelper;
import de.michiruf.allayfollowalways.helper.Teleport;
import net.minecraft.core.BlockPos;
//? if <=1.21.4 {
/*import net.minecraft.gametest.framework.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class BehaviourGameTest {

    /**
     * Places a 5x4x5 pool of the given block at the target Y, with a stone floor.
     * Returns the center position inside the pool (for teleporting into).
     */
    private Vec3 placePool(GameTestHelper context, ServerPlayer player, int yOffset,
                            net.minecraft.world.level.block.Block fluidBlock) {
        var world = context.getLevel();
        var px = (int) player.getX();
        var py = (int) player.getY();
        var pz = (int) player.getZ();
        var poolY = py + yOffset;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                world.setBlockAndUpdate(new BlockPos(px + dx, poolY - 1, pz + dz), Blocks.STONE.defaultBlockState());
                world.setBlockAndUpdate(new BlockPos(px + dx, poolY, pz + dz), fluidBlock.defaultBlockState());
                world.setBlockAndUpdate(new BlockPos(px + dx, poolY + 1, pz + dz), fluidBlock.defaultBlockState());
                world.setBlockAndUpdate(new BlockPos(px + dx, poolY + 2, pz + dz), fluidBlock.defaultBlockState());
            }
        }
        return new Vec3(px + 0.5, poolY + 0.5, pz + 0.5);
    }

    /**
     * A leashed allay should NOT teleport even when the player is further than teleportDistance,
     * because canFollowPlayer returns false when leashed.
     * Player is kept within leash range (~10 blocks) so the leash doesn't break.
     */
    /*? if <1.20.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
     *//*? } elif <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", skyAccess = true, timeoutTicks = 100000)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100000)
     /*?} */
    public void leashedAllayDoesNotTeleport(GameTestHelper context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);

        new TestExecutor(context, 3)
                .immediate(() -> {
                    TestConfigHelper.resetToDefaults();
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);

                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.createUniquePlayer();
                    holder.createAllayLinkedToPlayer();
                })
                .then(() -> {
                    holder.allay.setLeashedTo(holder.player, true);
                    check.assertTrue(holder.allay.isLeashed(), "Allay should be leashed");
                })
                .then(() -> {
                    // Move player 5 blocks up — above teleportDistance(1) but within leash range(~10)
                    Teleport.teleport(holder.player,
                            new Vec3(holder.player.getX(), holder.player.getY() + 5, holder.player.getZ()),
                            context.getLevel());
                })
                .then(() -> {
                    check.assertTrue(holder.allay.isLeashed(), "Allay should still be leashed");
                    var distance = holder.allay.position().distanceTo(holder.player.position());
                    check.assertTrue(distance > 1.0,
                            "Leashed allay should NOT teleport despite exceeding teleport distance. Distance: " + distance);
                })
                .immediate(check::complete)
                .runSync();
    }

    /**
     * Allay should NOT teleport when player is in water and avoidTeleportingIntoWater is enabled.
     * FakePlayer.tick() is a no-op, so we call baseTick() to update the fluid state.
     */
    /*? if <1.20.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
     *//*? } elif <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", skyAccess = true, timeoutTicks = 100000)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100000)
     /*?} */
    public void avoidTeleportIntoWater(GameTestHelper context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);

        new TestExecutor(context, 5)
                .immediate(() -> {
                    TestConfigHelper.resetToDefaults();
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWater(true);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.createUniquePlayer();
                    holder.createAllayLinkedToPlayer();
                })
                .then(() -> {
                    var dest = placePool(context, holder.player, 15, Blocks.WATER);
                    Teleport.teleport(holder.player, dest, context.getLevel());
                    // FakePlayer.tick() is empty, so baseTick() is never called and fluid state
                    // is never updated. Call it explicitly to update isTouchingWater().
                    holder.player.baseTick();
                })
                .then(() -> {
                    check.assertTrue(holder.player.isInWater(),
                            "Player should be touching water at " + holder.player.blockPosition());
                    var distance = holder.allay.position().distanceTo(holder.player.position());
                    check.assertTrue(distance > 10.0,
                            "Allay should NOT teleport to player in water. Distance: " + distance);
                })
                .immediate(check::complete)
                .runSync();
    }

    /**
     * Allay should NOT teleport when player is in lava and avoidTeleportingIntoLava is enabled.
     * FakePlayer.tick() is a no-op, so we call baseTick() to update the fluid state.
     */
    /*? if <1.20.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
     *//*? } elif <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", skyAccess = true, timeoutTicks = 100000)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100000)
            /*?} */
    public void avoidTeleportIntoLava(GameTestHelper context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);

        new TestExecutor(context, 5)
                .immediate(() -> {
                    TestConfigHelper.resetToDefaults();
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoLava(true);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.createUniquePlayer();
                    holder.createAllayLinkedToPlayer();
                })
                .then(() -> {
                    var dest = placePool(context, holder.player, 15, Blocks.LAVA);
                    Teleport.teleport(holder.player, dest, context.getLevel());
                    // FakePlayer.tick() is empty — call baseTick() to update isInLava()
                    holder.player.baseTick();
                })
                .then(() -> {
                    check.assertTrue(holder.player.isInLava(),
                            "Player should be in lava at " + holder.player.blockPosition());
                    var distance = holder.allay.position().distanceTo(holder.player.position());
                    check.assertTrue(distance > 10.0,
                            "Allay should NOT teleport to player in lava. Distance: " + distance);
                })
                .then(holder::cleanup)
                .immediate(check::complete)
                .runSync();
    }

    /**
     * Allay should NOT teleport when player is inside a wall and avoidTeleportingIntoWalls is enabled.
     * isInsideWall() is a live world check — no baseTick() needed.
     */
    /*? if <1.20.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", timeoutTicks = 100000)
     *//*? } elif <1.21.5 { */
    /*@GameTest(template = "fabric-gametest-api-v1:empty", skyAccess = true, timeoutTicks = 100000)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100000)
     /*?} */
    public void avoidTeleportIntoWalls(GameTestHelper context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);

        new TestExecutor(context, 5)
                .immediate(() -> {
                    context.killAllEntities();
                    TestConfigHelper.resetToDefaults();
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(true);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.createUniquePlayer();
                    holder.createAllayLinkedToPlayer();
                })
                .then(() -> {
                    // Encase the destination in solid blocks
                    var world = context.getLevel();
                    var px = (int) holder.player.getX();
                    var py = (int) holder.player.getY();
                    var pz = (int) holder.player.getZ();
                    var wallY = py + 15;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = 0; dy <= 2; dy++) {
                            for (int dz = -1; dz <= 1; dz++) {
                                world.setBlockAndUpdate(new BlockPos(px + dx, wallY + dy, pz + dz),
                                        Blocks.STONE.defaultBlockState());
                            }
                        }
                    }
                    // Teleport player into the wall
                    Teleport.teleport(holder.player,
                            new Vec3(px + 0.5, wallY, pz + 0.5),
                            context.getLevel());
                })
                .then(() -> {
                    // isInsideWall() checks the world live — no baseTick() needed
                    check.assertTrue(holder.player.isInWall(),
                            "Player should be inside a wall at " + holder.player.blockPosition());
                    var distance = holder.allay.position().distanceTo(holder.player.position());
                    check.assertTrue(distance > 10.0,
                            "Allay should NOT teleport to player in wall. Distance: " + distance);
                })
                .immediate(check::complete)
                .runSync();
    }

}
