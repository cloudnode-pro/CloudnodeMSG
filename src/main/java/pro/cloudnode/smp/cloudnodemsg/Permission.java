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
    public final static @NotNull String IGNORE_IMMUNE = "cloudnodemsg.ignore.bypass";
}
