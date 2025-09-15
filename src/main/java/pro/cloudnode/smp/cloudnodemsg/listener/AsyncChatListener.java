package pro.cloudnode.smp.cloudnodemsg.listener;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Message;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.command.TeamMessageCommand;
import pro.cloudnode.smp.cloudnodemsg.error.InvalidPlayerError;
import pro.cloudnode.smp.cloudnodemsg.error.NotInTeamError;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public final class AsyncChatListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void ignore(final @NotNull AsyncChatEvent event) {
        final @NotNull Set<@NotNull Audience> audience = event.viewers();
        final @NotNull Iterator<@NotNull Audience> iterator = audience.iterator();
        final @NotNull Player sender = event.getPlayer();
        final @NotNull Server server = CloudnodeMSG.getInstance().getServer();

        while (iterator.hasNext()) {
            final @NotNull Audience a = iterator.next();
            if (a instanceof final @NotNull Player player) {
                final @NotNull HashSet<@NotNull OfflinePlayer> ignored = Message.getIgnored(player).stream()
                        .map(server::getOfflinePlayer).collect(HashSet::new, HashSet::add, HashSet::addAll);

                if (ignored.contains(sender) && !sender.hasPermission(Permission.IGNORE_BYPASS)) iterator.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void channels(final @NotNull AsyncChatEvent event) {
        final @NotNull Player sender = event.getPlayer();
        final @NotNull Optional<@NotNull OfflinePlayer> channelRecipient = Message.getChannel(sender);
        if (channelRecipient.isEmpty()) return;
        event.setCancelled(true);
        try {
            new Message(sender, channelRecipient.get(), event.message()).send(Message.Context.CHANNEL);
        }
        catch (final @NotNull InvalidPlayerError e) {
            e.log().send(sender);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void teamChannel(final @NotNull AsyncChatEvent event) {
        final @NotNull Player sender = event.getPlayer();
        if (!Message.hasTeamChannel(sender)) return;
        event.setCancelled(true);
        final @NotNull Optional<@NotNull Team> team = Optional.ofNullable(sender.getScoreboard().getPlayerTeam(sender));
        if (team.isEmpty()) {
            Message.exitTeamChannel(sender);
            new NotInTeamError().send(sender);
            return;
        }
        TeamMessageCommand.sendTeamMessage(sender, team.get(), event.message());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void chatFormat(final @NotNull AsyncChatEvent event) {
        final @NotNull Optional<@NotNull ChatRenderer> format = CloudnodeMSG.getInstance().config().chatFormat();
        format.ifPresent(event::renderer);
    }
}
