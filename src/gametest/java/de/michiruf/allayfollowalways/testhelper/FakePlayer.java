package de.michiruf.allayfollowalways.testhelper;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.world.ServerWorld;

/**
 * A FakePlayer that works across all versions.
 * Extends Fabric's FakePlayer when available (>= 1.19.4),
 * otherwise extends ServerPlayerEntity directly.
 */
public class FakePlayer
        //? if <1.19.4 {
        /*extends net.minecraft.server.network.ServerPlayerEntity
        *///? } else {
        extends net.fabricmc.fabric.api.entity.FakePlayer
        //? }
{

    //? if <1.19.3 {
    /*public FakePlayer(ServerWorld world, GameProfile profile) {
        super(world.getServer(), world, profile, null);
        this.networkHandler = new FakeNetworkHandler(this);
    }
    *///? } elif <1.19.4 {
    /*public FakePlayer(ServerWorld world, GameProfile profile) {
        super(world.getServer(), world, profile);
        this.networkHandler = new FakeNetworkHandler(this);
    }
    *///? } else {
    public FakePlayer(ServerWorld world, GameProfile profile) {
        super(world, profile);
    }
    //? }

    //? if <1.19.4 {

    /*@Override
    public void tick() { }

    @Override
    public void increaseStat(net.minecraft.stat.Stat<?> stat, int amount) { }

    @Override
    public void resetStat(net.minecraft.stat.Stat<?> stat) { }

    @Override
    public boolean isInvulnerableTo(net.minecraft.entity.damage.DamageSource damageSource) {
        return true;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public net.minecraft.scoreboard.Team getScoreboardTeam() {
        return null;
    }

    @Override
    public void sleep(net.minecraft.util.math.BlockPos pos) { }

    @Override
    public boolean startRiding(net.minecraft.entity.Entity entity, boolean force) {
        return false;
    }

    @Override
    public java.util.OptionalInt openHandledScreen(@org.jetbrains.annotations.Nullable net.minecraft.screen.NamedScreenHandlerFactory factory) {
        return java.util.OptionalInt.empty();
    }

    @Override
    public void openHorseInventory(net.minecraft.entity.passive.AbstractHorseEntity horse, net.minecraft.inventory.Inventory inventory) { }

    private static class FakeNetworkHandler extends net.minecraft.server.network.ServerPlayNetworkHandler {
        private static final net.minecraft.network.ClientConnection FAKE_CONNECTION = new FakeConnection();

        public FakeNetworkHandler(net.minecraft.server.network.ServerPlayerEntity player) {
            super(player.getServer(), FAKE_CONNECTION, player);
        }

        @Override
        public void sendPacket(net.minecraft.network.Packet<?> packet) { }

        private static class FakeConnection extends net.minecraft.network.ClientConnection {
            private FakeConnection() {
                super(net.minecraft.network.NetworkSide.CLIENTBOUND);
            }

            @Override
            public void setPacketListener(net.minecraft.network.listener.PacketListener listener) { }
        }
    }
    *///? }
}
