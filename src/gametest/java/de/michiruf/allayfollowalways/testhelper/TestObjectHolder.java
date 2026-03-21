package de.michiruf.allayfollowalways.testhelper;

import com.mojang.authlib.GameProfile;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class TestObjectHolder {

    private final TestContext context;
    public FakePlayer player;
    public AllayEntity allay;
    public UUID allayUuid;
    private int allayId;

    public TestObjectHolder(TestContext context) {
        this.context = context;
    }

    public void createUniquePlayer() {
        var profile = new GameProfile(UUID.randomUUID(), "TestPlayer");
        player = new FakePlayer(context.getWorld(), profile);
        player.changeGameMode(GameMode.SURVIVAL);
        context.getWorld().onPlayerConnected(player);
    }

    public void createAllay() {
        allay = context.spawnEntity(EntityType.ALLAY, new BlockPos(0, 1, 0));
        allayUuid = allay.getUuid();
        allayId = allay.getId();
    }

    public void createAllayLinkedToPlayer() {
        createAllay();
        VersionedFabricTeleport.teleport(player, allay, context.getWorld());
        allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
    }

    public void cleanup() {
        player.remove(Entity.RemovalReason.DISCARDED);
        allay.remove(Entity.RemovalReason.DISCARDED);
    }

    /**
     * After cross-dimension teleport, the original entity is removed and a new one is created.
     * Use with waitUntil: first call {@link #allayRelinked} until it returns true,
     * then the allay field will be updated.
     */
    public boolean allayRelinked(ServerWorld world) {
        // Old allay first must have been removed
        if (!allay.isRemoved()) {
            return false;
        }

        var found = (AllayEntity) world.getEntity(allayUuid);

        if (found == null)
            found = (AllayEntity) world.getEntityById(allayId);

        if (found != null) {
            allay = found;
            return true;
        }

        return false;
    }
}
