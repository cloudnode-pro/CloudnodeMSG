package pro.cloudnode.smp.cloudnodemsg.message;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.InvalidPlayerError;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record Message(@NotNull OfflinePlayer sender, @NotNull OfflinePlayer recipient, @NotNull String message) {

    private @NotNull String playerOrServerUsername(final @NotNull OfflinePlayer player) throws InvalidPlayerError {
        if (player.getUniqueId().equals(console.getUniqueId()))
            return CloudnodeMSG.getInstance().config().consoleName();
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
        setReplyTo(sender, recipient);

        if (
                (recipient.isOnline() && Message.isIgnored(Objects.requireNonNull(recipient.getPlayer()), sender))
                &&
                (sender.isOnline() && !Objects.requireNonNull(sender.getPlayer()).hasPermission(Permission.IGNORE_BYPASS))
        ) return;
        sendMessage(recipient, CloudnodeMSG.getInstance().config()
                .incoming(senderUsername, recipientUsername, message));
        setReplyTo(recipient, sender);
    }

    public final static @NotNull OfflinePlayer console = CloudnodeMSG.getInstance().getServer()
            .getOfflinePlayer(UUID.fromString("00000000-0000-0000-0000-000000000000"));

    public static @NotNull OfflinePlayer offlinePlayer(final @NotNull CommandSender executor) {
        return executor instanceof final @NotNull Player player ? player : console;
    }

    public static void sendMessage(final @NotNull OfflinePlayer recipient, final @NotNull Component message) {
        if (recipient.getUniqueId() == console.getUniqueId())
            CloudnodeMSG.getInstance().getServer().getConsoleSender().sendMessage(message);
        else if (recipient.isOnline()) Objects.requireNonNull(recipient.getPlayer()).sendMessage(message);
    }

    private static @Nullable UUID consoleReply;

    public static final @NotNull NamespacedKey REPLY_TO = new NamespacedKey(CloudnodeMSG.getInstance(), "reply");

    public static void setReplyTo(final @NotNull OfflinePlayer sender, final @NotNull OfflinePlayer recipient) {
        if (sender.getUniqueId().equals(console.getUniqueId())) consoleReply = recipient.getUniqueId();
        else if (sender.isOnline())
            Objects.requireNonNull(sender.getPlayer()).getPersistentDataContainer()
                    .set(REPLY_TO, PersistentDataType.STRING, recipient.getUniqueId().toString());
    }

    public static @NotNull Optional<@NotNull OfflinePlayer> getReplyTo(final @NotNull OfflinePlayer player) {
        if (player.getUniqueId().equals(console.getUniqueId())) return Optional.ofNullable(consoleReply)
                .map(uuid -> CloudnodeMSG.getInstance().getServer().getOfflinePlayer(uuid));
        if (player.isOnline())
            return Optional.ofNullable(Objects.requireNonNull(player.getPlayer()).getPersistentDataContainer()
                    .get(REPLY_TO, PersistentDataType.STRING))
                    .map(uuid -> CloudnodeMSG.getInstance().getServer().getOfflinePlayer(UUID.fromString(uuid)));
        return Optional.empty();
    }

    public static void removeReplyTo(final @NotNull OfflinePlayer player) {
        if (player.getUniqueId().equals(console.getUniqueId())) consoleReply = null;
        else if (player.isOnline())
            Objects.requireNonNull(player.getPlayer()).getPersistentDataContainer().remove(REPLY_TO);
    }

    public static final @NotNull NamespacedKey IGNORED_PLAYERS = new NamespacedKey(CloudnodeMSG.getInstance(), "ignored");
    public static final @NotNull NamespacedKey CHANNEL_RECIPIENT = new NamespacedKey(CloudnodeMSG.getInstance(), "channel-recipient");

    /**
     * Get UUID set of ignored players from PDC string
     *
     * @param player The player
     */
    public static @NotNull HashSet<@NotNull UUID> getIgnored(final @NotNull Player player) {
        final @NotNull Optional<@NotNull String> str = Optional.ofNullable(player.getPersistentDataContainer().get(IGNORED_PLAYERS, PersistentDataType.STRING));
        return str.map(s -> new HashSet<>(Arrays.stream(s.split(";")).filter(e -> !e.isEmpty()).map(UUID::fromString).toList()))
                .orElseGet(HashSet::new);
    }

    /**
     * Check if a player is ignored
     *
     * @param player The player
     * @param ignored The ignored player
     */
    public static boolean isIgnored(final @NotNull Player player, final @NotNull OfflinePlayer ignored) {
        return getIgnored(player).contains(ignored.getUniqueId());
    }

    /**
     * Ignore a player
     *
     * @param player The player
     * @param ignore The player to ignore
     */
    public static void ignore(final @NotNull Player player, final @NotNull OfflinePlayer ignore) {
        final @NotNull HashSet<@NotNull UUID> ignoredPlayers = getIgnored(player);
        ignoredPlayers.add(ignore.getUniqueId());
        player.getPersistentDataContainer().set(IGNORED_PLAYERS, PersistentDataType.STRING, String.join(";", ignoredPlayers.stream().map(UUID::toString).toList()));
    }

    /**
     * Unignore a player
     *
     * @param player The player
     * @param ignored The player to unignore
     */
    public static void unignore(final @NotNull Player player, final @NotNull OfflinePlayer ignored) {
        final @NotNull HashSet<@NotNull UUID> ignoredPlayers = getIgnored(player);
        ignoredPlayers.remove(ignored.getUniqueId());
        player.getPersistentDataContainer().set(IGNORED_PLAYERS, PersistentDataType.STRING, String.join(";", ignoredPlayers.stream().map(UUID::toString).toList()));
    }

    /**
     * Create DM channel to player
     *
     * @param player    The player (you)
     * @param recipient The other end of the channel
     */
    public static void createChannel(final @NotNull Player player, final @NotNull OfflinePlayer recipient) {
        player.getPersistentDataContainer().set(CHANNEL_RECIPIENT, PersistentDataType.STRING, recipient.getUniqueId().toString());
    }

    /**
     * Exit DM channel
     *
     * @param player The player (you)
     */
    public static void exitChannel(final @NotNull Player player) {
        player.getPersistentDataContainer().remove(CHANNEL_RECIPIENT);
    }

    /**
     * Get DM channel recipient
     *
     * @param player The player
     */
    public static @NotNull Optional<@NotNull OfflinePlayer> getChannel(final @NotNull Player player) {
        return Optional.ofNullable(player.getPersistentDataContainer().get(CHANNEL_RECIPIENT, PersistentDataType.STRING))
                .map(uuid -> CloudnodeMSG.getInstance().getServer().getOfflinePlayer(UUID.fromString(uuid)));
    }
}
