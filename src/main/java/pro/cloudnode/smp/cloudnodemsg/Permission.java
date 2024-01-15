package pro.cloudnode.smp.cloudnodemsg;

import org.jetbrains.annotations.NotNull;

public final class Permission {
    /**
     * Allows using the /msg and /r commands
     */
    public final static @NotNull String USE = "cloudnodemsg.use";

    /**
     * Allows reloading the plugin
     */
    public final static @NotNull String RELOAD = "cloudnodemsg.reload";

    /**
     * Allows sending messages to vanished players
     */
    public final static @NotNull String SEND_VANISHED = "cloudnodemsg.send.vanished";

    /**
     * Allows ignoring and unignoring players
     */
    public final static @NotNull String IGNORE = "cloudnodemsg.ignore";

    /**
     * Player's messages are immune to ignoring
     */
    public final static @NotNull String IGNORE_BYPASS = "cloudnodemsg.ignore.bypass";

    /**
     * Allows using the /togglemsg command
     */
    public final static @NotNull String TOGGLE = "cloudnodemsg.toggle";

    /**
     * Allows using the /togglemsg command for others
     */
    public final static @NotNull String TOGGLE_OTHER = "cloudnodemsg.toggle.other";

    /**
     * Allows to send private message even when the target have their private messages toggled
     */
    public final static @NotNull String TOGGLE_BYPASS = "cloudnodemsg.toggle.bypass";
}
