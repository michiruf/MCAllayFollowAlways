package de.michiruf.allayfollowalways;

import de.michiruf.allayfollowalways.command.Command;
import de.michiruf.allayfollowalways.config.ConfigWrapper;
import de.michiruf.allayfollowalways.helper.ModLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * @author Michael Ruf
 * @since 2022-11-29
 */
public class AllayFollowAlwaysMod implements ModInitializer {

    public static final Logger LOGGER = new ModLogger("AllayFollowAlways");
    public static ConfigWrapper CONFIG;

    static {
        try {
            CONFIG = ConfigWrapper.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onInitialize() {
        LOGGER.warn("AllayFollowAlways is active");
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> Command.registerCommands(dispatcher));
    }
}
