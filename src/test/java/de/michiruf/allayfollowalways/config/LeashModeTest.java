package de.michiruf.allayfollowalways.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeashModeTest {

    @Test
    void allModesExist() {
        var modes = LeashMode.values();
        assertEquals(3, modes.length);
        assertNotNull(LeashMode.NONE);
        assertNotNull(LeashMode.FOLLOW);
        assertNotNull(LeashMode.DIRECTIONAL_SLOW_DOWN);
    }

    @Test
    void valueOfWorks() {
        assertEquals(LeashMode.NONE, LeashMode.valueOf("NONE"));
        assertEquals(LeashMode.FOLLOW, LeashMode.valueOf("FOLLOW"));
        assertEquals(LeashMode.DIRECTIONAL_SLOW_DOWN, LeashMode.valueOf("DIRECTIONAL_SLOW_DOWN"));
    }
}
