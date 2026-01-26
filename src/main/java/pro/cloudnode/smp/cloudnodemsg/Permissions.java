package pro.cloudnode.smp.cloudnodemsg;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayDeque;
import java.util.Queue;

@NullMarked
public final class Permissions {
    private static final String NAME = "cloudnodemsg";

    private static final Queue<Permission> REGISTRATION_QUEUE = new ArrayDeque<>();

    public static final Permission USE = create(
            "use",
            "Allows access to the private message (/msg) and reply (/r) commands",
            PermissionDefault.TRUE
    );

    public static final Permission USE_TEAM = create(
            "use.team",
            "Allows access to the team message command (/teammsg)",
            PermissionDefault.TRUE
    );

    public static final Permission IGNORE = create(
            "ignore",
            "Allows access to the /ignore and /unignore commands",
            PermissionDefault.TRUE
    );

    public static final Permission IGNORE_BYPASS = create(
            "ignore.bypass",
            "Prevents the sender’s messages from being ignored"
    );

    public static final Permission TOGGLE = create(
            "toggle",
            "Allows toggling receiving private messages on and off with /togglemsg",
            PermissionDefault.TRUE
    );

    public static final Permission TOGGLE_OTHER = create(
            "toggle.other",
            "Allows toggling receiving private messages on and off for others with /togglemsg <player>",
            PermissionDefault.OP
    );

    public static final Permission TOGGLE_BYPASS = create(
            "toggle.bypass",
            "Allows sending private messages to recipients who have toggled them off",
            PermissionDefault.OP
    );

    public static final Permission SEND_VANISHED = create(
            "send.vanished",
            "Allows sending messages to vanished recipients",
            PermissionDefault.OP
    );

    public static final Permission SPY = create(
            "spy",
            "Makes you see all private and team messages sent between all players"
    );

    public static final Permission RELOAD = create(
            "reload",
            "Allows access to the /cloudnodemsg reload command",
            PermissionDefault.OP
    );

    private static Permission create(final String node, final String description) {
        return new Permission(String.format("%s.%s", NAME, node), description);
    }

    private static Permission create(
            final String node,
            final String description,
            final PermissionDefault permissionDefault
    ) {
        final var permission = create(node, description);
        permission.setDefault(permissionDefault);
        REGISTRATION_QUEUE.add(permission);
        return permission;
    }

    public static void register() {
        final var pm = Bukkit.getPluginManager();
        while (!REGISTRATION_QUEUE.isEmpty()) {
            pm.addPermission(REGISTRATION_QUEUE.poll());
        }
    }
}
