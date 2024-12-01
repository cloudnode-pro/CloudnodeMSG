package pro.cloudnode.smp.cloudnodemsg.error;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * The player that messaged you is no longer online
 */
public final class ReplyOfflineError extends Error {
    /**
     * The player that messaged you is no longer online
     *
     * @param player The player's username
     */
    public ReplyOfflineError(final @NotNull OfflinePlayer player) {
        super(CloudnodeMSG.getInstance().config().replyOffline(player));
    }
}
