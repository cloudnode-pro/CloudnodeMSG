package pro.cloudnode.smp.cloudnodemsg;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scoreboard.Team;
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
    public @NotNull Component incoming(final @NotNull String sender, final @NotNull String recipient, final @NotNull Component message) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("incoming"))
                .replace("<sender>", sender)
                .replace("<recipient>", recipient),
                Placeholder.component("message", message)
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
    public @NotNull Component outgoing(final @NotNull String sender, final @NotNull String recipient, final @NotNull Component message) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("outgoing"))
                        .replace("<sender>", sender)
                        .replace("<recipient>", recipient),
                Placeholder.component("message", message)
        );
    }

    /**
     * Team message
     * <p>Uses the vanilla teams from `/team`</p>
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <team>} - team</li>
     *     <li>{@code <message>} - the message text</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param team The team
     * @param message The message text
     */
    public @NotNull Component team(final @NotNull String sender, final @NotNull Team team, final @NotNull Component message) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("team"))
                        .replace("<sender>", sender),
                Placeholder.component("team", team.displayName()),
                Placeholder.component("message", message)
        );
    }

    /**
     * Private message format as seen by people with the spy permission and console
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
    public @NotNull Component spy(final @NotNull String sender, final @NotNull String recipient, final @NotNull Component message) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("spy"))
                        .replace("<sender>", sender)
                        .replace("<recipient>", recipient),
                Placeholder.component("message", message)
        );
    }

    /**
     * Team message format as seen by people with the spy permission and console
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <team>} - team</li>
     *     <li>{@code <message>} - the message text</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param team The team
     * @param message The message text
     */
    public @NotNull Component teamSpy(final @NotNull String sender, final @NotNull Team team, final @NotNull Component message) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("team-spy"))
                        .replace("<sender>", sender),
                Placeholder.component("team", team.displayName()),
                Placeholder.component("message", message)
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
     * Message channel created
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <recipient>} - the username of the message recipient</li>
     *     <li>{@code <command>} - the command used, e.g. `msg`, `dm`, etc.</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param recipient The username of the message recipient
     * @param command The command used, e.g. `msg`, `dm`, etc.
     */
    public @NotNull Component channelCreated(final @NotNull String sender, final @NotNull String recipient, final @NotNull String command) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("channel.created"))
                        .replace("<sender>", sender)
                        .replace("<recipient>", recipient)
                        .replace("<command>", command));
    }

    /**
     * Message channel closed
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <recipient>} - the username of the message recipient</li>
     *     <li>{@code <command>} - the command used, e.g. `msg`, `dm`, etc.</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param recipient The username of the message recipient
     * @param command The command used, e.g. `msg`, `dm`, etc.
     */
    public @NotNull Component channelClosed(final @NotNull String sender, final @NotNull String recipient, final @NotNull String command) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("channel.closed"))
                        .replace("<sender>", sender)
                        .replace("<recipient>", recipient)
                        .replace("<command>", command));
    }

    /**
     * Message channel player is offline and channel closed
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <recipient>} - the username of the message recipient</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param recipient The username of the message recipient
     */
    public @NotNull Component channelOffline(final @NotNull String sender, final @NotNull String recipient) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("channel.offline"))
                        .replace("<sender>", sender)
                        .replace("<recipient>", recipient));
    }

    /**
     * Team chat channel created
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <team>} - team</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param team The team@
     */
    public @NotNull Component channelTeamCreated(final @NotNull String sender, final @NotNull Team team) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("channel.team-created"))
                        .replace("<sender>", sender),
                Placeholder.component("team", team.displayName()));
    }

    /**
     * Team chat channel closed
     * <p>Placeholders:</p>
     * <ul>
     *     <li>{@code <sender>} - the username of the message sender</li>
     *     <li>{@code <team>} - team</li>
     * </ul>
     *
     * @param sender The username of the message sender
     * @param team The team
     */
    public @NotNull Component channelTeamClosed(final @NotNull String sender, final @NotNull Team team) {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("channel.team-closed"))
                        .replace("<sender>", sender),
                Placeholder.component("team", team.displayName()));
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
     * Player's private messages have been toggled off
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
     * Player's private messages have been toggled on
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
     * You are trying to ignore yourself
     */
    public @NotNull Component ignoreYourself() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.ignore-yourself")));
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

    /**
     * Trying to message a team, but not in one
     */
    public @NotNull Component notInTeam() {
        return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(config.getString("errors.not-in-team")));
    }
}

