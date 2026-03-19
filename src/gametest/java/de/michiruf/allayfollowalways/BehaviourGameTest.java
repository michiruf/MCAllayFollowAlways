package de.michiruf.allayfollowalways;

//? if >= 1.19.4 {
import com.mojang.authlib.GameProfile;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
import de.michiruf.allayfollowalways.versioned.EntityHelper;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
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
                    // Teleport distance very low so allay would normally teleport even at short distances
                    AllayFollowAlwaysMod.CONFIG.teleportDistance(1f);
                    AllayFollowAlwaysMod.CONFIG.avoidTeleportingIntoWalls(false);
                    AllayFollowAlwaysMod.CONFIG.movementSpeedFactor(0);
                    holder.player = createUniquePlayer(context);
                    holder.allay = createAllayLinkedTo(context, holder.player);
                })
                .then(() -> {
                    // Leash the allay to the player
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
                .immediate(() -> context.complete())
                .run();
    }

    //? }
}
