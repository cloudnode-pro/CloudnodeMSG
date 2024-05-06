package pro.cloudnode.smp.cloudnodemsg.error;

import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Player not found
 */
public final class PlayerNotFoundError extends Error {
    /**
     * Player not found
     *
     * @param player the player's username
     */
    public PlayerNotFoundError(final @NotNull String player) {
        super(CloudnodeMSG.getInstance().config().playerNotFound(player));
    }
}
