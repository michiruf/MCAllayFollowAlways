package de.michiruf.allayfollowalways.versioned;

import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;

/**
 * @author Michael Ruf
 * @since 2025-12-31
 */
public class PermissionHelper {

    public static boolean hasPermission(ServerCommandSource cmd) {
        return cmd.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS));
    }
}
