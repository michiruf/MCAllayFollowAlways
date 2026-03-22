package de.michiruf.allayfollowalways.testhelper;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;

/**
 * A FakePlayer that works across all versions.
 * Extends Fabric's FakePlayer when available (>= 1.19.4),
 * otherwise extends ServerPlayerEntity directly.
 */
public class FakePlayer
        //? if <1.19.4 {
        /*extends net.minecraft.server.level.ServerPlayer
        *///? } else {
        extends net.fabricmc.fabric.api.entity.FakePlayer
        //? }
{

    //? if <1.19.3 {
    /*public FakePlayer(ServerLevel world, GameProfile profile) {
        super(world.getServer(), world, profile, null);
        this.connection = new FakeNetworkHandler(this);
    }
    *///? } elif <1.19.4 {
    /*public FakePlayer(ServerLevel world, GameProfile profile) {
        super(world.getServer(), world, profile);
        this.connection = new FakeNetworkHandler(this);
    }
    *///? } else {
    public FakePlayer(ServerLevel world, GameProfile profile) {
        super(world, profile);
    }
    //? }

    //? if <1.19.4 {

    /*@Override
    public void tick() { }

    @Override
    public void awardStat(net.minecraft.stats.Stat<?> stat, int amount) { }

    @Override
    public void resetStat(net.minecraft.stats.Stat<?> stat) { }

    @Override
    public boolean isInvulnerableTo(net.minecraft.world.damagesource.DamageSource damageSource) {
        return true;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public net.minecraft.world.scores.PlayerTeam getTeam() {
        return null;
    }

    @Override
    public void startSleeping(net.minecraft.core.BlockPos pos) { }

    @Override
    public boolean startRiding(net.minecraft.world.entity.Entity entity, boolean force) {
        return false;
    }

    @Override
    public java.util.OptionalInt openMenu(@org.jetbrains.annotations.Nullable net.minecraft.world.MenuProvider factory) {
        return java.util.OptionalInt.empty();
    }

    @Override
    public void openHorseInventory(net.minecraft.world.entity.animal.horse.AbstractHorse horse, net.minecraft.world.Container inventory) { }

    private static class FakeNetworkHandler extends net.minecraft.server.network.ServerGamePacketListenerImpl {
        private static final net.minecraft.network.Connection FAKE_CONNECTION = new FakeConnection();

        public FakeNetworkHandler(net.minecraft.server.level.ServerPlayer player) {
            super(player.getServer(), FAKE_CONNECTION, player);
        }

        @Override
        public void send(net.minecraft.network.protocol.Packet<?> packet) { }

        private static class FakeConnection extends net.minecraft.network.Connection {
            private FakeConnection() {
                super(net.minecraft.network.protocol.PacketFlow.CLIENTBOUND);
            }

            @Override
            public void setListener(net.minecraft.network.PacketListener listener) { }
        }
    }
    *///? }
}
