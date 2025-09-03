package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Mail;
import pro.cloudnode.smp.cloudnodemsg.Message;
import pro.cloudnode.smp.cloudnodemsg.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class MailCommand extends Command {

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.MAIL)) return sendMessage(sender, CloudnodeMSG.getInstance().config().noPermission());
        if (args.length == 0) return help(sender);
        final @NotNull String @NotNull [] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
        final @NotNull String subCommandLabel = label + " " + args[0];
        return switch (args[0]) {
            case "send", "new", "to" -> send(sender, subCommandLabel, subCommandArgs);
            default -> help(sender);
        };
    }

    public static boolean help(final @NotNull CommandSender sender) {
        // TODO
        return false;
    }

    public static boolean send(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String[] args) {
        if (args.length == 0) return sendMessage(sender, CloudnodeMSG.getInstance().config().usage(label, "<recipient> <message>"));
        if (args.length == 1) return sendMessage(sender, CloudnodeMSG.getInstance().config().usage(label, args[0] + " <message>"));
        final @NotNull String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        final @NotNull OfflinePlayer recipient = CloudnodeMSG.getInstance().getServer().getOfflinePlayer(args[0]);
        if (!recipient.hasPlayedBefore()) return sendMessage(sender, CloudnodeMSG.getInstance().config().playerNotFound(Message.name(recipient)));
        final @NotNull Mail mail = new Mail(Message.offlinePlayer(sender), recipient, message);
        mail.insert();
        final @NotNull OfflinePlayer senderOfflinePlayer = Message.offlinePlayer(sender);
        final @NotNull Optional<@NotNull Player> senderPlayer = Optional.ofNullable(senderOfflinePlayer.getPlayer());
        final @NotNull Optional<@NotNull Player> recipientPlayer = Optional.ofNullable(recipient.getPlayer());
        if (recipientPlayer.isPresent() && (!Message.isIgnored(recipientPlayer.get(), senderOfflinePlayer) || senderPlayer.isEmpty() || senderPlayer.get().hasPermission(Permission.IGNORE_BYPASS)))
            sendMessage(recipientPlayer.get(), CloudnodeMSG.getInstance().config().mailReceived(mail));
        return sendMessage(sender, CloudnodeMSG.getInstance().config().mailSent(mail));
    }

    @Override
    public @Nullable List<@NotNull String> tab(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String @NotNull [] args) {
        return null;
    }
}
