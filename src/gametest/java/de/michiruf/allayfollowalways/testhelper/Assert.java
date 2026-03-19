package de.michiruf.allayfollowalways.testhelper;

//? if >= 1.19.4 {
import com.mojang.authlib.GameProfile;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.UUID;
//? }
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;

public class Assert {

    private final TestContext context;

    public Assert(TestContext context) {
        this.context = context;
    }

    //? if >= 1.19.4 {
    public FakePlayer createUniquePlayer() {
        var profile = new GameProfile(UUID.randomUUID(), "TestPlayer");
        var player = FakePlayer.get(context.getWorld(), profile);
        player.changeGameMode(GameMode.SURVIVAL);
        context.getWorld().onPlayerConnected(player);
        return player;
    }

    public AllayEntity createAllayLinkedTo(FakePlayer player) {
        var allay = context.spawnEntity(EntityType.ALLAY, new BlockPos(0, 1, 0));
        VersionedFabricTeleport.teleport(player, allay, context.getWorld());
        allay.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
        return allay;
    }
    //? }

    public void assertTrue(boolean condition, String message) {
        if (!condition)
            throw createException(message);
    }

    public void assertFalse(boolean condition, String message) {
        if (condition)
            throw createException(message);
    }

    public GameTestException createException(String message) {
        //? if <=1.21.4 {
        /*return new GameTestException(message);
        *///? } else {
        return new GameTestException(Text.literal(message), (int) context.getTick());
        //? }
    }

    public void complete() {
        context.complete();
    }
}
