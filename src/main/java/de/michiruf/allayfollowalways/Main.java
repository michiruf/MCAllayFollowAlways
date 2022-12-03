package de.michiruf.allayfollowalways;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Michael Ruf
 * @since 2022-11-29
 */
public class Main implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("AllayFollowAlways");
    public static final de.michiruf.allayfollowalways.Config CONFIG = de.michiruf.allayfollowalways.Config.createAndLoad();

    @Override
    public void onInitialize() {
        LOGGER.info("AllayFollowAlways is active");
        CommandRegistrationCallback.EVENT.register(Command::registerCommands);
    }
}
