package de.michiruf.allayfollowalways.OLD_TRY;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Michael Ruf
 * @since 2022-11-30
 */
public interface PlayerBeforeSwitchWorldCallback {
    Event<PlayerBeforeSwitchWorldCallback> EVENT = EventFactory.createArrayBacked(PlayerBeforeSwitchWorldCallback.class,
            (callbacks) -> (player) -> {
                for (PlayerBeforeSwitchWorldCallback callback : callbacks) {
                    callback.onSwitchWorld(player);
                }
            });

    /**
     * Called on the server when a player is switching worlds.
     *
     * @param player The player that will switch the world.
     */
    void onSwitchWorld(ServerPlayerEntity player);
}
