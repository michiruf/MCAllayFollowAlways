package de.michiruf.allayfollowalways.versioned;

//? if >=1.21.11 {
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
//? }
import net.minecraft.server.command.ServerCommandSource;

/**
 * @author Michael Ruf
 * @since 2025-12-31
 */
public class PermissionHelper {

    public static boolean hasPermission(ServerCommandSource cmd) {
        //? if <1.21.11 {
        /*return cmd.hasPermissionLevel(4);
        *///? } else {
        return cmd.getPermissions().hasPermission(new Permission.Level(PermissionLevel.OWNERS));
        //? }
    }
}
