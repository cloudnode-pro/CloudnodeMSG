package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.NotIgnoredError;
import pro.cloudnode.smp.cloudnodemsg.error.NotPlayerError;
import pro.cloudnode.smp.cloudnodemsg.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class UnIgnoreCommand extends Command {
    public static final @NotNull String usage = "<player>";

    @Override
    public boolean run(final @NotNull CommandSender sender, final @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.IGNORE)) return new NoPermissionError().send(sender);
        if (!(sender instanceof final @NotNull Player player)) return new NotPlayerError().send(sender);
        if (args.length == 0) return sendMessage(sender, CloudnodeMSG.getInstance().config().usage(label, usage));
        final @NotNull OfflinePlayer target = CloudnodeMSG.getInstance().getServer().getOfflinePlayer(args[0]);
        if (Message.isIgnored(player, target)) return IgnoreCommand.unignore(player, target);
        return new NotIgnoredError(target).send(sender);
    }

    @Override
    public @Nullable List<@NotNull String> onTabComplete(final @NotNull CommandSender sender, final @NotNull org.bukkit.command.Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (args.length == 1 && sender.hasPermission(Permission.IGNORE) && sender instanceof final @NotNull Player player) {
            final @NotNull HashSet<@NotNull UUID> ignored = Message.getIgnored(player);
            final @NotNull Server server = CloudnodeMSG.getInstance().getServer();
            return new ArrayList<>(ignored.stream().map(u -> server.getOfflinePlayer(u).getName())
                    .filter(Objects::nonNull).filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase())).toList());
        }
        return new ArrayList<>();
    }
}
