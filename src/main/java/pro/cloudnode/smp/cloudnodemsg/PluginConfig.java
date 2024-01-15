package pro.cloudnode.smp.cloudnodemsg;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class PluginConfig {
    public @NotNull FileConfiguration config;

    public PluginConfig(final @NotNull FileConfiguration config) {
        this.config = config;
    }

    /**
     * Incoming message format (recipient's point of view)
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <recipient>} - the username of the message recipient</li>
     *     <li>{@code <message>} - the message text</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param recipient The username of the message recipient
     * @param message The message text
     */
    public @NotNull Component incoming(final @NotNull String sender, final @NotNull String recipient, final @NotNull String message) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("incoming"))
                .replace("<sender>", sender)
                .replace("<recipient>", recipient),
                Placeholder.unparsed("message", message)
        );
    }

    /**
     * Outgoing message format (sender's point of view)
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <recipient>} - the username of the message recipient</li>
     *     <li>{@code <message>} - the message text</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param recipient The username of the message recipient
     * @param message The message text
     */
    public @NotNull Component outgoing(final @NotNull String sender, final @NotNull String recipient, final @NotNull String message) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("outgoing"))
                        .replace("<sender>", sender)
                        .replace("<recipient>", recipient),
                Placeholder.unparsed("message", message)
        );
    }

    /**
     * Player has successfully been ignored
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the username of the player</li></ul>
     *
     * @param player The username of the player
     */
    public @NotNull Component ignored(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("ignored")),
                Placeholder.unparsed("player", player)
        );
    }

    /**
     * Player has successfully been unignored
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the username of the player</li></ul>
     *
     * @param player The username of the player
     */
    public @NotNull Component unignored(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("unignored")),
                Placeholder.unparsed("player", player)
        );
    }

    /**
     * Command usage format
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <command>} - the command name</li>
     *     <li>{@code <usage>} - the command usage parameters</li>
     * </ul>
     *
     * @param label Command label
     * @param usage Command usage parameters
     */
    public @NotNull Component usage(final @NotNull String label, final @NotNull String usage) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("usage")),
                Placeholder.unparsed("command", label),
                Placeholder.unparsed("usage", usage)
        );
    }

    /**
     * Plugin reloaded
     */
    public @NotNull Component reloaded() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("reloaded")));
    }

    /**
     * Name for console/server that should appear as {@code <sender>} or {@code <recipient>} in messages
     */
    public @NotNull String consoleName() {
        return Objects.requireNonNull(config.getString("console-name"));
    }

    public @NotNull Component toggleDisable() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("toggle.disable.message")));
    }

    /**
     * The player you disabled private messages
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the player's username</li></ul>
     *
     * @param player the player's username
     */
    public @NotNull Component toggleDisableOther(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("toggle.disable.other")),
                Placeholder.unparsed("player", player)
        );
    }

    public @NotNull Component toggleEnable() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("toggle.enable.message")));
    }

    /**
     * The player you enabled private messages
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the player's username</li></ul>
     *
     * @param player the player's username
     */
    public @NotNull Component toggleEnableOther(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("toggle.enable.other")),
                Placeholder.unparsed("player", player)
        );
    }

    /**
     * No permission
     */
    public @NotNull Component noPermission() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.no-permission")));
    }

    /**
     * Player has no username (somehow)
     */
    public @NotNull Component invalidPlayer() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.invalid-player")));
    }

    /**
     * Player not found
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the player's username</li></ul>
     *
     * @param player The player's username
     */
    public @NotNull Component playerNotFound(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.player-not-found")),
                Placeholder.unparsed("player", player)
        );
    }

    public @NotNull Component messageYourself() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.message-yourself")));
    }

    public @NotNull Component nobodyReply() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.nobody-reply")));
    }

    /**
     * The player that messaged you is no longer online
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the player's username</li></ul>
     *
     * @param player The player's username
     */
    public @NotNull Component replyOffline(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.reply-offline")),
                Placeholder.unparsed("player", player)
        );
    }

    /**
     * Only players can use this command
     */
    public @NotNull Component notPlayer() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.not-player")));
    }

    /**
     * That player is not ignored
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the player's username</li></ul>
     *
     * @param player The player's username
     */
    public @NotNull Component notIgnored(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.not-ignored")),
                Placeholder.unparsed("player", player)
        );
    }

    /**
     * Player cannot be ignored
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the player's username</li></ul>
     *
     * @param player The player's username
     */
    public @NotNull Component cannotIgnore(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.cannot-ignore")),
                Placeholder.unparsed("player", player)
        );
    }

    /**
     * Target player has never joined the server
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the player's username</li></ul>
     *
     * @param player The player's username
     */
    public @NotNull Component neverJoined(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.never-joined")),
                Placeholder.unparsed("player", player)
        );
    }

    /**
     * Target player have disabled their incoming private messages.
     * <p>Placeholders:</p>
     * <ul><li>{@code <player>} - the player's username</li></ul>
     *
     * @param player The player's username
     */
    public @NotNull Component incomingDisabled(final @NotNull String player) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.incoming-disabled")),
                Placeholder.unparsed("player", player)
        );
    }
}

