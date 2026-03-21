package de.michiruf.allayfollowalways.helper;

//? if >=1.21.11 {
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
//? }
import net.minecraft.commands.CommandSourceStack;

/**
 * @author Michael Ruf
 * @since 2025-12-31
 */
public class PermissionHelper {

    public static boolean hasPermission(CommandSourceStack cmd) {
        //? if <1.21.11 {
        /*return cmd.hasPermission(4);
        *///? } else {
        return cmd.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS));
        //? }
    }
}
