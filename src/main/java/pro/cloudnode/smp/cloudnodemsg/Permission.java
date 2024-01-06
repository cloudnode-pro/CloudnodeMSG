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
}
