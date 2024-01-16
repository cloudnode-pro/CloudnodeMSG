package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.InvalidPlayerError;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.NobodyReplyError;
import pro.cloudnode.smp.cloudnodemsg.error.ReplyOfflineError;
import pro.cloudnode.smp.cloudnodemsg.error.PlayerHasIncomingDisabledError;
import pro.cloudnode.smp.cloudnodemsg.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ReplyCommand extends Command {
    public static final @NotNull String usage = "<message>";

    @Override
    public boolean run(final @NotNull CommandSender sender, final @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.USE)) return new NoPermissionError().send(sender);
        if (args.length == 0) return sendMessage(sender, CloudnodeMSG.getInstance().config().usage(label, usage));

        final @NotNull Optional<@NotNull OfflinePlayer> recipient = Message.getReplyTo(Message.offlinePlayer(sender));
        if (recipient.isEmpty()) return new NobodyReplyError().send(sender);
        if (!recipient.get().getUniqueId().equals(Message.console.getUniqueId()) && !Message.isIncomingEnabled(Objects.requireNonNull(recipient.get().getPlayer())) && !sender.hasPermission(Permission.TOGGLE_BYPASS)) return new PlayerHasIncomingDisabledError(Objects.requireNonNull(recipient.get().getName())).send(sender);
        if (
            !recipient.get().getUniqueId().equals(Message.console.getUniqueId())
            && (
                    !recipient.get().isOnline()
                    || (CloudnodeMSG.isVanished(Objects.requireNonNull(recipient.get().getPlayer())) && !sender.hasPermission(Permission.SEND_VANISHED))
            )
        )
            return new ReplyOfflineError(Optional.ofNullable(recipient.get().getName()).orElse("Unknown Player")).send(sender);

        try {
            new Message(Message.offlinePlayer(sender), recipient.get(), String.join(" ", args)).send();
            return true;
        }
        catch (final @NotNull InvalidPlayerError e) {
            return e.log().send(sender);
        }
    }

    @Override
    public @NotNull List<@NotNull String> onTabComplete(final @NotNull CommandSender sender, final @NotNull org.bukkit.command.Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        return new ArrayList<>();
    }
}
