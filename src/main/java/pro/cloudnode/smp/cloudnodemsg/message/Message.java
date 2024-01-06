package pro.cloudnode.smp.cloudnodemsg.message;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.error.InvalidPlayerError;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Message {
    public final @NotNull OfflinePlayer sender;
    public final @NotNull OfflinePlayer recipient;
    public final @NotNull String message;

    public Message(final @NotNull OfflinePlayer sender, final @NotNull OfflinePlayer recipient, final @NotNull String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    private @NotNull String playerOrServerUsername(final @NotNull OfflinePlayer player) throws InvalidPlayerError {
        if (player.getUniqueId().equals(console.getUniqueId())) return CloudnodeMSG.getInstance().config().consoleName();
        else {
            final @NotNull Optional<@NotNull String> name = Optional.ofNullable(player.getName());
            if (name.isEmpty()) throw new InvalidPlayerError();
            else return name.get();
        }
    }

    public void send() throws InvalidPlayerError {
        final @NotNull String senderUsername = playerOrServerUsername(this.sender);
        final @NotNull String recipientUsername = playerOrServerUsername(this.recipient);

        sendMessage(sender, CloudnodeMSG.getInstance().config().outgoing(senderUsername, recipientUsername, message));
        sendMessage(recipient, CloudnodeMSG.getInstance().config().incoming(senderUsername, recipientUsername, message));
    }

    public final static @NotNull OfflinePlayer console = CloudnodeMSG.getInstance().getServer()
            .getOfflinePlayer(UUID.fromString("00000000-0000-0000-0000-000000000000"));

    public static @NotNull OfflinePlayer offlinePlayer(final @NotNull CommandSender executor) {
        return executor instanceof final @NotNull Player player ? player : console;
    }

    public static void sendMessage(final @NotNull OfflinePlayer recipient, final @NotNull Component message) {
        if (recipient.getUniqueId() == console.getUniqueId()) CloudnodeMSG.getInstance().getServer().getConsoleSender().sendMessage(message);
        else if (recipient.isOnline()) Objects.requireNonNull(recipient.getPlayer()).sendMessage(message);
    }
}
