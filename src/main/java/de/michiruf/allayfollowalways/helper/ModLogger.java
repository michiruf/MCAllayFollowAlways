package de.michiruf.allayfollowalways.helper;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.config.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * Purpose-based logger with per-category log levels.
 * Each method targets a category and requires a {@link LogLevel}.
 * The message is emitted only if the category's configured level is
 * equal to or higher than the requested level.
 *
 * @author Michael Ruf
 * @since 2026-03-22
 */
public class ModLogger {

    private final Logger delegate;

    public ModLogger(String name) {
        this.delegate = LoggerFactory.getLogger(name);
    }

    public void mod(LogLevel level, String msg, Object... args) {
        log(AllayFollowAlwaysMod.CONFIG::logModLevel, level, msg, args);
    }

    public void teleport(LogLevel level, String msg, Object... args) {
        log(AllayFollowAlwaysMod.CONFIG::logTeleportLevel, level, msg, args);
    }

    public void tick(LogLevel level, String msg, Object... args) {
        log(AllayFollowAlwaysMod.CONFIG::logTickLevel, level, msg, args);
    }

    public void warn(String msg, Object... args) {
        delegate.warn(msg, args);
    }

    public void error(String msg, Object... args) {
        delegate.error(msg, args);
    }

    private void log(Supplier<LogLevel> configLevel, LogLevel required, String msg, Object... args) {
        if (AllayFollowAlwaysMod.CONFIG != null
                && configLevel.get().isEnabled(required))
            delegate.info(msg, args);
    }
}
