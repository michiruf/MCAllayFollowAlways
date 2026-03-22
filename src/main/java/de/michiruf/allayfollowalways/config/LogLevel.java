package de.michiruf.allayfollowalways.config;

/**
 * @author Michael Ruf
 * @since 2026-03-22
 */
public enum LogLevel {

    OFF(0),
    INFO(1),
    DEBUG(2);

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    public boolean isEnabled(LogLevel required) {
        return this.level >= required.level;
    }
}
