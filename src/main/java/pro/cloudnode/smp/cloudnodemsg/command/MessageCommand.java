package pro.cloudnode.smp.cloudnodemsg.command;

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

        final @NotNull Optional<@NotNull Player> recipient = Optional.ofNullable(CloudnodeMSG.getInstance().getServer()
                .getPlayer(args[0]));
        if (recipient.isEmpty() || (CloudnodeMSG.isVanished(recipient.get()) && !sender.hasPermission(Permission.SEND_VANISHED))) return new PlayerNotFoundError(args[0]).send(sender);
        if (sender instanceof final @NotNull Player player && recipient.get().getUniqueId().equals(player.getUniqueId()))
            return new MessageYourselfError().send(sender);
        if (!Message.isIncomingEnabled(recipient.get()) && !sender.hasPermission(Permission.TOGGLE_BYPASS)) return new PlayerHasIncomingDisabledError(recipient.get().getName()).send(sender);

        if (args.length == 1) {
            final @NotNull Player player = (Player) sender;
            if (Message.getChannel(player).map(r -> r.getUniqueId().equals(recipient.get().getUniqueId())).orElse(false)) {
                Message.exitChannel(player);
                return sendMessage(player, CloudnodeMSG.getInstance().config().channelClosed(player.getName(), recipient.get().getName(), label));
            }
            Message.createChannel(player, recipient.get());
            return sendMessage(player, CloudnodeMSG.getInstance().config().channelCreated(player.getName(), recipient.get().getName(), label));
        }

        try {
            new Message(Message.offlinePlayer(sender), recipient.get(), String.join(" ", Arrays.copyOfRange(args, 1, args.length))).send();
            return true;
        }
        catch (final @NotNull InvalidPlayerError e) {
            return e.log().send(sender);
        }
    }

    @Override
    public @Nullable List<@NotNull String> onTabComplete(final @NotNull CommandSender sender, final @NotNull org.bukkit.command.Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.USE)) return new ArrayList<>();
        // `null` works for list of players
        if (args.length == 1) return null;
        return new ArrayList<>();
    }
}
