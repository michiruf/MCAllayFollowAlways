package de.michiruf.allayfollowalways.testhelper;

import com.mojang.authlib.GameProfile;
import de.michiruf.allayfollowalways.helper.EntityHelper;
import de.michiruf.allayfollowalways.helper.Teleport;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.GameType;

public class TestObjectHolder {

    private final GameTestHelper context;
    public FakePlayer player;
    public Allay allay;
    public UUID allayUuid;
    private int allayId;

    public TestObjectHolder(GameTestHelper context) {
        this.context = context;
    }

    public void createUniquePlayer() {
        var profile = new GameProfile(UUID.randomUUID(), "TestPlayer");
        player = new FakePlayer(context.getLevel(), profile);
        player.setGameMode(GameType.SURVIVAL);
        context.getLevel().addNewPlayer(player);
    }

    public void createAllay() {
        allay = context.spawn(EntityType.ALLAY, new BlockPos(0, 1, 0));
        allayUuid = allay.getUUID();
        allayId = allay.getId();
    }

    public void createAllayLinkedToPlayer() {
        createAllay();
        Teleport.teleport(player, allay, context.getLevel());
        allay.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
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
    public boolean allayRelinked(ServerLevel world) {
        // Old allay first must have been removed
        // In some versions, the entity is moved directly without being recreated,
        // so we also check if the allay is already in the target world
        if (!allay.isRemoved() && EntityHelper.getLevel(allay) != world) {
            return false;
        }

        var found = (Allay) world.getEntity(allayUuid);

        if (found == null)
            found = (Allay) world.getEntity(allayId);

        if (found != null) {
            allay = found;
            return true;
        }

        return false;
    }
}
