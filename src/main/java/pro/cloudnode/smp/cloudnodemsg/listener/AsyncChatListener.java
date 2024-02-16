package pro.cloudnode.smp.cloudnodemsg.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.InvalidPlayerError;
import pro.cloudnode.smp.cloudnodemsg.Message;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public final class AsyncChatListener implements Listener {
    @EventHandler (priority = EventPriority.LOWEST)
    public void ignore(final @NotNull AsyncChatEvent event) {
        final @NotNull Set<@NotNull Audience> audience = event.viewers();
        final @NotNull Iterator<@NotNull Audience> iterator = audience.iterator();
        final @NotNull Player sender = event.getPlayer();

        while (iterator.hasNext()) {
            final @NotNull Audience a = iterator.next();
            if (a instanceof final @NotNull Player player) {
                final @NotNull Server server = CloudnodeMSG.getInstance().getServer();
                final @NotNull HashSet<@NotNull OfflinePlayer> ignored = Message.getIgnored(player).stream().map(server::getOfflinePlayer).collect(HashSet::new, HashSet::add, HashSet::addAll);

                if (ignored.contains(sender) && !sender.hasPermission(Permission.IGNORE_BYPASS)) iterator.remove();
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void channels(final @NotNull AsyncChatEvent event) {
        final @NotNull Player sender = event.getPlayer();
        final @NotNull Optional<@NotNull OfflinePlayer> channelRecipient = Message.getChannel(sender);
        if (channelRecipient.isEmpty()) return;
        event.setCancelled(true);
        final @NotNull Optional<@NotNull Player> recipient = channelRecipient.map(p -> p.isOnline() ? p.getPlayer() : null);
        if (recipient.isEmpty()) {
            Message.exitChannel(sender);
            sender.sendMessage(CloudnodeMSG.getInstance().config().channelOffline(sender, channelRecipient.get()));
            return;
        }

        try {
            new Message(sender, recipient.get(), event.message()).send();
        }
        catch (final @NotNull InvalidPlayerError e) {
            e.log().send(sender);
        }
    }
}
