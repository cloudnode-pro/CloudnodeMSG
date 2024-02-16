package pro.cloudnode.smp.cloudnodemsg.error;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Target player has never joined the server
 */
public final class NeverJoinedError extends Error {
    /**
     * Target player has never joined the server
     *
     * @param player the player's username
     */
    public NeverJoinedError(final @NotNull OfflinePlayer player) {
        super(CloudnodeMSG.getInstance().config().neverJoined(player));
    }
}
