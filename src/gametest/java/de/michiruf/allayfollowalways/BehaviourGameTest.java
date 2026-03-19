package de.michiruf.allayfollowalways;

//? if >= 1.19.4 {
import com.mojang.authlib.GameProfile;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
import de.michiruf.allayfollowalways.versioned.EntityHelper;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.test.GameTestException;
import net.minecraft.text.Text;
//? if <=1.21.4 {
/*import net.minecraft.test.GameTest;
*///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.UUID;
//? }

@SuppressWarnings("unused")
public class BehaviourGameTest {

    //? if >= 1.19.4 {

    private static void check(TestContext context, boolean condition, String message) {
        if (!condition) {
            //? if <=1.21.4 {
            /*throw new GameTestException(message);
             *///? } else {
            throw new GameTestException(Text.literal(message), (int) context.getTick());
            //? }
        }
    }

    private FakePlayer createUniquePlayer(TestContext context) {
        var profile = new GameProfile(UUID.randomUUID(), "TestPlayer");
        var player = FakePlayer.get(context.getWorld(), profile);
        player.changeGameMode(GameMode.SURVIVAL);
        context.getWorld().onPlayerConnected(player);
        return player;
    }

    private AllayEntity createAllayLinkedTo(TestContext context, FakePlayer player) {
        var allay = context.spawnEntity(EntityType.ALLAY, new BlockPos(0, 1, 0));
        VersionedFabricTeleport.teleport(player, allay, context.getWorld());
        allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
        return allay;
    }

    /**
     * Places a 5x4x5 pool of the given block at the target Y, with a stone floor.
     * Returns the center position inside the pool (for teleporting into).
     */
    private Vec3d placePool(TestContext context, FakePlayer player, int yOffset,
                            net.minecraft.block.Block fluidBlock) {
        var world = context.getWorld();
        var px = (int) player.getX();
        var py = (int) player.getY();
        var pz = (int) player.getZ();
        var poolY = py + yOffset;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                world.setBlockState(new BlockPos(px + dx, poolY - 1, pz + dz), Blocks.STONE.getDefaultState());
                world.setBlockState(new BlockPos(px + dx, poolY, pz + dz), fluidBlock.getDefaultState());
                world.setBlockState(new BlockPos(px + dx, poolY + 1, pz + dz), fluidBlock.getDefaultState());
                world.setBlockState(new BlockPos(px + dx, poolY + 2, pz + dz), fluidBlock.getDefaultState());
            }
        }
        return new Vec3d(px + 0.5, poolY + 0.5, pz + 0.5);
    }

    /**
     * A leashed allay should NOT teleport even when the player is further than teleportDistance,
     * because canFollowPlayer returns false when leashed.
     * Player is kept within leash range (~10 blocks) so the leash doesn't break.
     */
    /*? if <1.20.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
     *//*? } elif <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100)
            /*?} */
    public void leashedAllayDoesNotTeleport(TestContext context) {
        final var holder = new Object() {
            FakePlayer player;
            AllayEntity allay;
        };

        new TestExecutor(context, 3)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.player = createUniquePlayer(context);
                    holder.allay = createAllayLinkedTo(context, holder.player);
                })
                .then(() -> {
                    holder.allay.attachLeash(holder.player, true);
                    check(context, holder.allay.isLeashed(), "Allay should be leashed");
                })
                .then(() -> {
                    // Move player 5 blocks up — above teleportDistance(1) but within leash range(~10)
                    VersionedFabricTeleport.teleport(holder.player,
                            new Vec3d(holder.player.getX(), holder.player.getY() + 5, holder.player.getZ()),
                            context.getWorld());
                })
                .then(() -> {
                    check(context, holder.allay.isLeashed(), "Allay should still be leashed");
                    var distance = EntityHelper.getPos(holder.allay).distanceTo(EntityHelper.getPos(holder.player));
                    check(context, distance > 1.0,
                            "Leashed allay should NOT teleport despite exceeding teleport distance. Distance: " + distance);
                })
                .immediate(context::complete)
                .run();
    }

    /**
     * Allay should NOT teleport when player is in water and avoidTeleportingIntoWater is enabled.
     * FakePlayer.tick() is a no-op, so we call baseTick() to update the fluid state.
     */
    /*? if <1.20.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
     *//*? } elif <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100)
            /*?} */
    public void avoidTeleportIntoWater(TestContext context) {
        final var holder = new Object() {
            FakePlayer player;
            AllayEntity allay;
        };

        new TestExecutor(context, 5)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWater(true);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.player = createUniquePlayer(context);
                    holder.allay = createAllayLinkedTo(context, holder.player);
                })
                .then(() -> {
                    var dest = placePool(context, holder.player, 15, Blocks.WATER);
                    VersionedFabricTeleport.teleport(holder.player, dest, context.getWorld());
                    // FakePlayer.tick() is empty, so baseTick() is never called and fluid state
                    // is never updated. Call it explicitly to update isTouchingWater().
                    holder.player.baseTick();
                })
                .then(() -> {
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWater(true);
                    check(context, holder.player.isTouchingWater(),
                            "Player should be touching water at " + holder.player.getBlockPos());
                    var distance = EntityHelper.getPos(holder.allay).distanceTo(EntityHelper.getPos(holder.player));
                    check(context, distance > 10.0,
                            "Allay should NOT teleport to player in water. Distance: " + distance);
                })
                .immediate(context::complete)
                .run();
    }

    /**
     * Allay should NOT teleport when player is in lava and avoidTeleportingIntoLava is enabled.
     * FakePlayer.tick() is a no-op, so we call baseTick() to update the fluid state.
     */
    /*? if <1.20.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
     *//*? } elif <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100)
            /*?} */
    public void avoidTeleportIntoLava(TestContext context) {
        final var holder = new Object() {
            FakePlayer player;
            AllayEntity allay;
        };

        new TestExecutor(context, 5)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoLava(true);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.player = createUniquePlayer(context);
                    holder.allay = createAllayLinkedTo(context, holder.player);
                })
                .then(() -> {
                    var dest = placePool(context, holder.player, 15, Blocks.LAVA);
                    VersionedFabricTeleport.teleport(holder.player, dest, context.getWorld());
                    // FakePlayer.tick() is empty — call baseTick() to update isInLava()
                    holder.player.baseTick();
                })
                .then(() -> {
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoLava(true);
                    check(context, holder.player.isInLava(),
                            "Player should be in lava at " + holder.player.getBlockPos());
                    var distance = EntityHelper.getPos(holder.allay).distanceTo(EntityHelper.getPos(holder.player));
                    check(context, distance > 10.0,
                            "Allay should NOT teleport to player in lava. Distance: " + distance);
                })
                .immediate(context::complete)
                .run();
    }

    /**
     * Allay should NOT teleport when player is inside a wall and avoidTeleportingIntoWalls is enabled.
     * isInsideWall() is a live world check — no baseTick() needed.
     */
    /*? if <1.20.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
     *//*? } elif <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
     *//*?} else { */
    @GameTest(skyAccess = true, maxTicks = 100)
            /*?} */
    public void avoidTeleportIntoWalls(TestContext context) {
        final var holder = new Object() {
            FakePlayer player;
            AllayEntity allay;
        };

        new TestExecutor(context, 5)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(true);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.player = createUniquePlayer(context);
                    holder.allay = createAllayLinkedTo(context, holder.player);
                })
                .then(() -> {
                    // Encase the destination in solid blocks
                    var world = context.getWorld();
                    var px = (int) holder.player.getX();
                    var py = (int) holder.player.getY();
                    var pz = (int) holder.player.getZ();
                    var wallY = py + 15;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = 0; dy <= 2; dy++) {
                            for (int dz = -1; dz <= 1; dz++) {
                                world.setBlockState(new BlockPos(px + dx, wallY + dy, pz + dz),
                                        Blocks.STONE.getDefaultState());
                            }
                        }
                    }
                    // Teleport player into the wall
                    VersionedFabricTeleport.teleport(holder.player,
                            new Vec3d(px + 0.5, wallY, pz + 0.5),
                            context.getWorld());
                })
                .then(() -> {
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(true);
                    // isInsideWall() checks the world live — no baseTick() needed
                    check(context, holder.player.isInsideWall(),
                            "Player should be inside a wall at " + holder.player.getBlockPos());
                    var distance = EntityHelper.getPos(holder.allay).distanceTo(EntityHelper.getPos(holder.player));
                    check(context, distance > 10.0,
                            "Allay should NOT teleport to player in wall. Distance: " + distance);
                })
                .immediate(context::complete)
                .run();
    }

    //? }
}
