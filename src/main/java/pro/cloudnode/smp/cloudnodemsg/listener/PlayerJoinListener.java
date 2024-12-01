package pro.cloudnode.smp.cloudnodemsg.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Mail;
import pro.cloudnode.smp.cloudnodemsg.Permission;

public final class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        CloudnodeMSG.runAsync(() -> {
            if (!CloudnodeMSG.getInstance().config().mailNotifyOnLogin()) return;
            final @NotNull Player player = event.getPlayer();
            if (!player.hasPermission(Permission.MAIL)) return;
            final int unread = Mail.unread(player);
            if (unread > 0) player.sendMessage(CloudnodeMSG.getInstance().config().mailNotify(unread));
        });
    }
}
