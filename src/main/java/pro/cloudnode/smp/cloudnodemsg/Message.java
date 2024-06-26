package pro.cloudnode.smp.cloudnodemsg;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.error.ChannelOfflineError;
import pro.cloudnode.smp.cloudnodemsg.error.InvalidPlayerError;
import pro.cloudnode.smp.cloudnodemsg.error.PlayerHasIncomingDisabledError;
import pro.cloudnode.smp.cloudnodemsg.error.PlayerNotFoundError;
import pro.cloudnode.smp.cloudnodemsg.error.ReplyOfflineError;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class Message {
    private final @NotNull OfflinePlayer sender;
    private final @NotNull OfflinePlayer recipient;
    private final @NotNull Component message;

    public Message(@NotNull OfflinePlayer sender, @NotNull OfflinePlayer recipient, @NotNull Component message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public Message(@NotNull OfflinePlayer sender, @NotNull OfflinePlayer recipient, @NotNull String message) {
        this(sender, recipient, Component.text(message));
    }

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
        send(Context.REGULAR);
    }

    public void send(final @NotNull Context context) throws InvalidPlayerError {
        final @NotNull String senderUsername = playerOrServerUsername(this.sender);
        final @NotNull String recipientUsername = playerOrServerUsername(this.recipient);

        final @NotNull Optional<@NotNull Player> senderPlayer = Optional.ofNullable(this.sender.getPlayer());
        final @NotNull Optional<@NotNull Player> recipientPlayer = Optional.ofNullable(this.recipient.getPlayer());

        if (!recipient.getUniqueId().equals(console.getUniqueId()) && recipientPlayer.isEmpty() || (recipientPlayer.isPresent() && senderPlayer.isPresent() && CloudnodeMSG.isVanished(recipientPlayer.get()) && !senderPlayer.get().hasPermission(Permission.SEND_VANISHED))) {
            if (context == Context.CHANNEL) {
                final @NotNull Player player = Objects.requireNonNull(sender.getPlayer());
                Message.exitChannel(player);
                new ChannelOfflineError(player.getName(), Optional.ofNullable(recipient.getName())
                        .orElse("Unknown Player")).send(player);
            }
            else {
                final @NotNull Audience senderAudience = senderPlayer.isPresent() ? senderPlayer.get() : CloudnodeMSG.getInstance().getServer().getConsoleSender();
                if (context == Context.REPLY) new ReplyOfflineError(recipientUsername).send(senderAudience);
                else new PlayerNotFoundError(recipientUsername).send(senderAudience);
            }
            return;
        }

        if (recipientPlayer.isPresent() && senderPlayer.isPresent() && !Message.isIncomingEnabled(recipientPlayer.get()) && !senderPlayer
                .get().hasPermission(Permission.TOGGLE_BYPASS)) {
            new PlayerHasIncomingDisabledError(recipientPlayer.get().getName()).send(senderPlayer.get());
            return;
        }

        sendSpyMessage(sender, recipient, message);
        sendMessage(sender, CloudnodeMSG.getInstance().config().outgoing(senderUsername, recipientUsername, message));
        if ((recipientPlayer.isPresent() && Message.isIgnored(recipientPlayer.get(), sender)) && (senderPlayer.isPresent() && !senderPlayer
                .get().hasPermission(Permission.IGNORE_BYPASS))) return;
        sendMessage(recipient, CloudnodeMSG.getInstance().config()
                .incoming(senderUsername, recipientUsername, message));

        if (sender.getUniqueId().equals(console.getUniqueId()) || (senderPlayer.isPresent() && !Message.hasChannel(senderPlayer.get(), recipient)))
            setReplyTo(sender, recipient);
        if (recipient.getUniqueId().equals(console.getUniqueId()) || (recipientPlayer.isPresent() && !Message.hasChannel(recipientPlayer.get(), sender)))
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

    /**
     * Send social spy to online players with permission
     */
    public static void sendSpyMessage(final @NotNull OfflinePlayer sender, final @NotNull OfflinePlayer recipient, final @NotNull Component message) {
        final @NotNull String senderName = sender.getUniqueId().equals(console.getUniqueId()) ? CloudnodeMSG
                .getInstance().config().consoleName() : Optional.ofNullable(sender.getName()).orElse("Unknown Player");
        final @NotNull String recipientName = recipient.getUniqueId().equals(console.getUniqueId()) ? CloudnodeMSG
                .getInstance().config().consoleName() : Optional.ofNullable(recipient.getName())
                                                      .orElse("Unknown Player");
        for (final @NotNull Player player : CloudnodeMSG.getInstance().getServer().getOnlinePlayers()) {
            if (!player.hasPermission(Permission.SPY) || player.getUniqueId().equals(sender.getUniqueId()) || player
                    .getUniqueId().equals(recipient.getUniqueId())) continue;
            sendMessage(player, CloudnodeMSG.getInstance().config().spy(senderName, recipientName, message));
        }
        if (!sender.getUniqueId().equals(console.getUniqueId()) && !recipient.getUniqueId()
                .equals(console.getUniqueId()))
            sendMessage(console, CloudnodeMSG.getInstance().config().spy(senderName, recipientName, message));
    }

    private static @Nullable UUID consoleReply;

    public static final @NotNull NamespacedKey REPLY_TO = new NamespacedKey(CloudnodeMSG.getInstance(), "reply");

    public static void setReplyTo(final @NotNull OfflinePlayer sender, final @NotNull OfflinePlayer recipient) {
        if (sender.getUniqueId().equals(console.getUniqueId())) consoleReply = recipient.getUniqueId();
        else if (sender.isOnline()) Objects.requireNonNull(sender.getPlayer()).getPersistentDataContainer()
                .set(REPLY_TO, PersistentDataType.STRING, recipient.getUniqueId().toString());
    }

    public static @NotNull Optional<@NotNull OfflinePlayer> getReplyTo(final @NotNull OfflinePlayer player) {
        if (player.getUniqueId().equals(console.getUniqueId())) return Optional.ofNullable(consoleReply)
                .map(uuid -> CloudnodeMSG.getInstance().getServer().getOfflinePlayer(uuid));
        if (player.isOnline()) return Optional
                .ofNullable(Objects.requireNonNull(player.getPlayer()).getPersistentDataContainer()
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
    public static final @NotNull NamespacedKey CHANNEL_TEAM = new NamespacedKey(CloudnodeMSG.getInstance(), "channel-team");

    /**
     * Get UUID set of ignored players from PDC string
     *
     * @param player The player
     */
    public static @NotNull HashSet<@NotNull UUID> getIgnored(final @NotNull Player player) {
        final @NotNull Optional<@NotNull String> str = Optional.ofNullable(player.getPersistentDataContainer()
                .get(IGNORED_PLAYERS, PersistentDataType.STRING));
        return str.map(s -> new HashSet<>(Arrays.stream(s.split(";")).filter(e -> !e.isEmpty()).map(UUID::fromString)
                .toList())).orElseGet(HashSet::new);
    }

    /**
     * Check if a player is ignored
     *
     * @param player  The player
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
        player.getPersistentDataContainer()
                .set(IGNORED_PLAYERS, PersistentDataType.STRING, String.join(";", ignoredPlayers.stream()
                        .map(UUID::toString).toList()));
    }

    /**
     * Unignore a player
     *
     * @param player  The player
     * @param ignored The player to unignore
     */
    public static void unignore(final @NotNull Player player, final @NotNull OfflinePlayer ignored) {
        final @NotNull HashSet<@NotNull UUID> ignoredPlayers = getIgnored(player);
        ignoredPlayers.remove(ignored.getUniqueId());
        player.getPersistentDataContainer()
                .set(IGNORED_PLAYERS, PersistentDataType.STRING, String.join(";", ignoredPlayers.stream()
                        .map(UUID::toString).toList()));
    }

    public static final @NotNull NamespacedKey INCOMING_ENABLED = new NamespacedKey(CloudnodeMSG.getInstance(), "incoming_enabled");

    /**
     * Allows player to receive private messages
     *
     * @param player The player
     */
    public static void incomingEnable(final @NotNull Player player) {
        player.getPersistentDataContainer().set(INCOMING_ENABLED, PersistentDataType.BOOLEAN, true);
    }

    /**
     * Denies player to receive private messages
     *
     * @param player The player
     */
    public static void incomingDisable(final @NotNull Player player) {
        player.getPersistentDataContainer().set(INCOMING_ENABLED, PersistentDataType.BOOLEAN, false);
    }

    /**
     * Check if a player allows private messages
     *
     * @param player The player
     */
    public static boolean isIncomingEnabled(final @NotNull Player player) {
        return player.getPersistentDataContainer().getOrDefault(INCOMING_ENABLED, PersistentDataType.BOOLEAN, true);
    }

    /**
     * Create DM channel to player
     *
     * @param player    The player (you)
     * @param recipient The other end of the channel
     */
    public static void createChannel(final @NotNull Player player, final @NotNull OfflinePlayer recipient) {
        player.getPersistentDataContainer()
                .set(CHANNEL_RECIPIENT, PersistentDataType.STRING, recipient.getUniqueId().toString());
        player.getPersistentDataContainer().remove(CHANNEL_TEAM);
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
        return Optional
                .ofNullable(player.getPersistentDataContainer().get(CHANNEL_RECIPIENT, PersistentDataType.STRING))
                .map(uuid -> CloudnodeMSG.getInstance().getServer().getOfflinePlayer(UUID.fromString(uuid)));
    }

    /**
     * Check whether player has DM channel with recipient
     *
     * @param player    The player
     * @param recipient The recipient
     */
    public static boolean hasChannel(final @NotNull Player player, final @NotNull OfflinePlayer recipient) {
        final @NotNull Optional<@NotNull OfflinePlayer> channel = getChannel(player);
        return channel.isPresent() && channel.get().getUniqueId().equals(recipient.getUniqueId());
    }

    /**
     * Check whether player has DM channel
     *
     * @param player The player
     */
    public static boolean hasChannel(final @NotNull Player player) {
        return getChannel(player).isPresent();
    }

    /**
     * Team message channel
     *
     * @param player The player
     */
    public static void createTeamChannel(final @NotNull Player player) {
        player.getPersistentDataContainer().set(CHANNEL_TEAM, PersistentDataType.BOOLEAN, true);
        player.getPersistentDataContainer().remove(CHANNEL_RECIPIENT);
    }

    /**
     * Exit team message channel
     *
     * @param player The player
     */
    public static void exitTeamChannel(final @NotNull Player player) {
        player.getPersistentDataContainer().remove(CHANNEL_TEAM);
    }

    /**
     * Check whether player has a team message channel
     *
     * @param player The player
     */
    public static boolean hasTeamChannel(final @NotNull Player player) {
        return player.getPersistentDataContainer().getOrDefault(CHANNEL_TEAM, PersistentDataType.BOOLEAN, false);
    }

    /**
     * The context in which this message is sent
     */
    public static enum Context {
        /**
         * Message sent via command (i.e. no special context)
         */
        REGULAR,

        /**
         * Message sent via messaging channel
         */
        CHANNEL,

        /**
         * Message sent as a reply
         */
        REPLY;
    }
}
