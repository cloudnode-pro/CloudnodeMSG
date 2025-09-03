package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Message;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.InvalidPlayerError;
import pro.cloudnode.smp.cloudnodemsg.error.MessageYourselfError;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.PlayerHasIncomingDisabledError;
import pro.cloudnode.smp.cloudnodemsg.error.PlayerNotFoundError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class MessageCommand extends Command {
    public static final @NotNull String usage = "<player> [message]";
    public static final @NotNull String usageConsole = "<player> <message>";

    @Override
    public boolean run(final @NotNull CommandSender sender, final @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.USE)) return new NoPermissionError().send(sender);
        if (args.length == 0)
            return sendMessage(sender, CloudnodeMSG.getInstance().config().usage(label, sender instanceof Player ? usage : usageConsole));
        if (args.length == 1 && !(sender instanceof Player)) return sendMessage(sender, CloudnodeMSG.getInstance().config()
                    .usage(label, usageConsole.replace("<player>", args[0])));

        final @NotNull Optional<@NotNull Player> recipient = Optional.ofNullable(CloudnodeMSG.getInstance().getServer()
                .getPlayer(args[0]));
        if (sender instanceof final @NotNull Player player && recipient.isPresent() && recipient.get().getUniqueId().equals(player.getUniqueId()))
            return new MessageYourselfError().send(sender);
        if (args.length == 1) {
            final @NotNull Player player = (Player) sender;
            if (Message.hasChannel(player)) {
                final @NotNull OfflinePlayer recipientOffline = CloudnodeMSG.getInstance().getServer().getOfflinePlayer(args[0]);
                if (Message.getChannel(player).map(r -> r.getUniqueId().equals(recipientOffline.getUniqueId())).orElse(false)) {
                    Message.exitChannel(player);
                    return sendMessage(player, CloudnodeMSG.getInstance().config().channelClosed(player, recipientOffline, label));
                }
            }
            if (recipient.isEmpty() || (CloudnodeMSG.isVanished(recipient.get()) && !player.hasPermission(Permission.SEND_VANISHED))) return new PlayerNotFoundError(args[0]).send(player);
            if (!Message.isIncomingEnabled(recipient.get())) return new PlayerHasIncomingDisabledError(recipient.get()).send(player);
            Message.createChannel(player, recipient.get());
            return sendMessage(player, CloudnodeMSG.getInstance().config().channelCreated(player, recipient.get(), label));
        }

        try {
            if (recipient.isEmpty()) return new PlayerNotFoundError(args[0]).send(sender);
            new Message(Message.offlinePlayer(sender), recipient.get(), String.join(" ", Arrays.copyOfRange(args, 1, args.length))).send();
            return true;
        }
        catch (final @NotNull InvalidPlayerError e) {
            return e.log().send(sender);
        }
    }

    @Override
    public @Nullable List<@NotNull String> tab(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.USE)) return new ArrayList<>();
        // `null` works for list of players
        if (args.length == 1) return null;
        return new ArrayList<>();
    }
}
