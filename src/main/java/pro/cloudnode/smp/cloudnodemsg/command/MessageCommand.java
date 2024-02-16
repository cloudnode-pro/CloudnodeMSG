package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.InvalidPlayerError;
import pro.cloudnode.smp.cloudnodemsg.error.MessageYourselfError;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.PlayerNotFoundError;
import pro.cloudnode.smp.cloudnodemsg.error.PlayerHasIncomingDisabledError;
import pro.cloudnode.smp.cloudnodemsg.Message;

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
        if (args.length == 0) {
            return sendMessage(sender, CloudnodeMSG.getInstance().config().usage(label, sender instanceof Player ? usage : usageConsole));
        }
        if (args.length == 1 && !(sender instanceof Player)) return sendMessage(sender, CloudnodeMSG.getInstance().config()
                    .usage(label, usageConsole.replace("<player>", args[0])));

        final @NotNull OfflinePlayer recipient = CloudnodeMSG.getInstance().getServer().getOfflinePlayer(args[0]);
        final @NotNull Optional<@NotNull Player> onlineRecipient = Optional.ofNullable(recipient.getPlayer());
        if (onlineRecipient.isEmpty() || (CloudnodeMSG.isVanished(onlineRecipient.get()) && !sender.hasPermission(Permission.SEND_VANISHED))) return new PlayerNotFoundError(recipient).send(sender);
        if (sender instanceof final @NotNull Player player && recipient.getUniqueId().equals(player.getUniqueId()))
            return new MessageYourselfError().send(sender);
        if (!Message.isIncomingEnabled(onlineRecipient.get()) && !sender.hasPermission(Permission.TOGGLE_BYPASS)) return new PlayerHasIncomingDisabledError(recipient).send(sender);

        if (args.length == 1) {
            final @NotNull Player player = (Player) sender;
            if (Message.getChannel(player).map(r -> r.getUniqueId().equals(recipient.getUniqueId())).orElse(false)) {
                Message.exitChannel(player);
                return sendMessage(player, CloudnodeMSG.getInstance().config().channelClosed(player, recipient, label));
            }
            Message.createChannel(player, recipient);
            return sendMessage(player, CloudnodeMSG.getInstance().config().channelCreated(player, recipient, label));
        }

        try {
            new Message(Message.offlinePlayer(sender), recipient, String.join(" ", Arrays.copyOfRange(args, 1, args.length))).send();
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
