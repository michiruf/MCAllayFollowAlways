package de.michiruf.allayfollowalways.helper;

import de.michiruf.allayfollowalways.AllayFollowAlwaysMod;
import de.michiruf.allayfollowalways.config.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.AbstractLogger;

/**
 * Logger that respects the mod's configurable {@link LogLevel}.
 * <p>
 * Delegates to an SLF4J logger. Info and debug output use {@link Level#INFO}
 * on the delegate, since Minecraft filters SLF4J debug by default.
 * Warnings and errors always pass through regardless of the configured level.
 *
 * @author Michael Ruf
 * @since 2026-03-22
 */
public class ModLogger extends AbstractLogger {

    private final Logger delegate;

    public ModLogger(String name) {
        this.delegate = LoggerFactory.getLogger(name);
    }

    private boolean isEnabled(LogLevel required) {
        return AllayFollowAlwaysMod.CONFIG != null
                && AllayFollowAlwaysMod.CONFIG.logLevel().ordinal() >= required.ordinal();
    }

    @Override
    public boolean isTraceEnabled() {
        return isEnabled(LogLevel.DEBUG);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return isEnabled(LogLevel.DEBUG);
    }

    @Override
    public boolean isDebugEnabled() {
        return isEnabled(LogLevel.DEBUG);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return isEnabled(LogLevel.DEBUG);
    }

    @Override
    public boolean isInfoEnabled() {
        return isEnabled(LogLevel.INFO);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return isEnabled(LogLevel.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return delegate.isWarnEnabled(marker);
    }

    @Override
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return delegate.isErrorEnabled(marker);
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {
        // Route everything through delegate.info since MC filters debug/trace
        switch (level) {
            case ERROR -> delegate.error(marker, messagePattern, arguments);
            case WARN -> delegate.warn(marker, messagePattern, arguments);
            default -> delegate.info(marker, messagePattern, arguments);
        }
    }
}
