package de.michiruf.allayfollowalways.testhelper;

import com.mojang.authlib.GameProfile;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class TestObjectHolder {

    //? if >= 1.19.4 {

    private final TestContext context;
    public FakePlayer player;
    public AllayEntity allay;
    public UUID allayUuid;

    public TestObjectHolder(TestContext context) {
        this.context = context;
    }

    public void createUniquePlayer() {
        var profile = new GameProfile(UUID.randomUUID(), "TestPlayer");
        player = FakePlayer.get(context.getWorld(), profile);
        player.changeGameMode(GameMode.SURVIVAL);
        context.getWorld().onPlayerConnected(player);
    }

    public void createAllay() {
        allay = context.spawnEntity(EntityType.ALLAY, new BlockPos(0, 1, 0));
        allayUuid = allay.getUuid();
    }

    public void createAllayLinkedToPlayer() {
        createAllay();
        VersionedFabricTeleport.teleport(player, allay, context.getWorld());
        allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
    }

    /**
     * After cross-dimension teleport, the original entity is removed and a new one is created
     */
    public void relinkAllayForWorld(ServerWorld world) {
        allay = (AllayEntity) world.getEntity(allayUuid);
    }

    //? }
}
